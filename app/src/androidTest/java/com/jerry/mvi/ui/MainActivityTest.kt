package com.jerry.mvi.ui

import android.os.SystemClock
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import com.google.gson.Gson
import com.jerry.mvi.R

import okhttp3.mockwebserver.*
import org.hamcrest.Matcher
import com.jerry.mvi.unit.*
import getHoodieListResponse
import java.net.HttpURLConnection
import androidx.test.rule.ActivityTestRule
import getSneakerListResponse


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    lateinit var mockWebServer: MockWebServer

    @Rule
    @JvmField
    var mMainActivityResult = ActivityTestRule(MainActivity::class.java, true, false)


    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }


    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun test_all_success() {
        mockNetworkResponse("hoodie_list", HttpURLConnection.HTTP_OK)
        mockNetworkResponse("sneaker_List", HttpURLConnection.HTTP_OK)
        mMainActivityResult.launchActivity(null)
        //waiting loading
        SystemClock.sleep(2000)

        //Check if item at 0th position is having 0th element in json
        Espresso.onView(withId(R.id.rvProduct))
            .check(
                ViewAssertions.matches(
                    recyclerItemAtPosition(
                        0,
                        ViewMatchers.hasDescendant(ViewMatchers.withText("HoodieName1"))
                    )
                )
            )
        Espresso.onView(withId(R.id.rvProduct))
            .check(
                ViewAssertions.matches(
                    recyclerItemAtPosition(
                        1,
                        ViewMatchers.hasDescendant(ViewMatchers.withText("HoodieName2"))
                    )
                )
            )
        Espresso.onView(withId(R.id.rvProduct))
            .check(
                ViewAssertions.matches(
                    recyclerItemAtPosition(
                        2,
                        ViewMatchers.hasDescendant(ViewMatchers.withText("SneakerName1"))
                    )
                )
            )
    }

    @Test
    fun test_fail_case() {
        mockNetworkResponse("hoodie_list", 500)
        mockNetworkResponse("sneaker_List", 500)
        mMainActivityResult.launchActivity(null)
        //waiting loading
        SystemClock.sleep(2000)

        //check contain 500
        Espresso.onView(withId(android.R.id.message)).check(
            matches(ViewMatchers.withSubstring("500"))
        )

        SystemClock.sleep(500)

    }

    fun getJson(type: String):String {
        if ("hoodie_list".equals(type))
            return Gson().toJson(getHoodieListResponse())
        else if ("sneaker_List".equals(type))
            return Gson().toJson(getSneakerListResponse())
        return ""
    }

    fun mockNetworkResponse(path: String, responseCode: Int) = mockWebServer.enqueue(
        MockResponse()
            .setResponseCode(responseCode)
            .setBody(getJson(path))
    )


    inner class ClickOnButtonView : ViewAction {
        internal var click = ViewActions.click()

        override fun getConstraints(): Matcher<View> {
            return click.constraints
        }

        override fun getDescription(): String {
            return " click on custom button view"
        }

        override fun perform(uiController: UiController, view: View) {
            //btnClickMe -> Custom row item view button
            click.perform(uiController, view.findViewById(R.id.sectionItemContainer))
        }
    }
}