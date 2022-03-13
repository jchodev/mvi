package com.jerry.mvi.repository

import com.jerry.mvi.BuildConfig

import com.jerry.mvi.data.api.ProductApi
import com.jerry.mvi.data.repository.NetworkRepository
import getHoodieListResponse
import getSneakerListResponse

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class NetworkRepositoryTest {
    private val dispatcher = UnconfinedTestDispatcher()

    private val mockProductApi: ProductApi = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_hoodie_list_success() {
        runTest {
            whenever(mockProductApi.getHoodieList()).thenReturn(getHoodieListResponse())
            val mockNetworkRepository = NetworkRepository(mockProductApi)
            val mockResponse = mockNetworkRepository.getHoodieList()

            val actualTitle = mockResponse.products.size
            val expectedTitle = getHoodieListResponse().products.size

            assertEquals(expectedTitle, actualTitle)
        }
    }

    @Test
    fun test_sneaker_list_success() {
        runTest {
            whenever(mockProductApi.getSneakerList()).thenReturn(getSneakerListResponse())
            val mockNetworkRepository = NetworkRepository(mockProductApi)
            val mockResponse = mockNetworkRepository.getSneakerList()

            val actualTitle = mockResponse.products.size
            val expectedTitle = getSneakerListResponse().products.size

            assertEquals(expectedTitle, actualTitle)
        }
    }
}