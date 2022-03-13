package com.jerry.mvi.viewmodel

import app.cash.turbine.test
import com.jerry.mvi.base.ViewState
import com.jerry.mvi.data.api.ProductApi
import com.jerry.mvi.data.model.Product
import com.jerry.mvi.data.repository.NetworkRepository
import com.jerry.mvi.ui.product.intent.ProductListIntent
import com.jerry.mvi.ui.product.viewmodel.ProductListViewModel
import getHoodieListResponse
import getSneakerListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.ArgumentMatchers.anyString

import org.mockito.Mockito.`when`
import org.mockito.kotlin.given
import java.io.IOException

@ExperimentalCoroutinesApi
class ProductListModelTest {
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
    fun test_with_success() = runBlockingTest {
        launch {
            whenever(mockProductApi.getSneakerList()).thenReturn(getSneakerListResponse())
            whenever(mockProductApi.getHoodieList()).thenReturn(getHoodieListResponse())
            val mNetworkRepository = NetworkRepository(mockProductApi)
            val mockViewModel = ProductListViewModel(mNetworkRepository)
            mockViewModel.productListViewState.test {
                Assert.assertEquals(ViewState.Initial, awaitItem())
                Assert.assertEquals(ViewState.Loading, awaitItem())

                Assert.assertEquals(
                    ViewState.Success(
                        listOf(
                            getHoodieListResponse().products,
                            getSneakerListResponse().products
                        )
                    ),
                    awaitItem()
                )
                awaitComplete()
            }
            mockViewModel.productListIntent.send(ProductListIntent.GetProductList)
        }
    }

}