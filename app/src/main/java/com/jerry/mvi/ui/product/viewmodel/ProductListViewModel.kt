package com.jerry.mvi.ui.product.viewmodel

import com.jerry.mvi.base.BaseViewModel
import com.jerry.mvi.base.ViewState
import com.jerry.mvi.data.model.Product
import com.jerry.mvi.data.model.ProductListResponse
import com.jerry.mvi.data.repository.NetworkRepository
import com.jerry.mvi.ui.product.intent.ProductListIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val networkRepository : NetworkRepository
) : BaseViewModel() {

    //from UI Intentchannel
    val productListIntent = Channel<ProductListIntent>(Channel.UNLIMITED)

    private val _productListViewState = MutableStateFlow<ViewState<List<Product>>>(ViewState.Initial)
    val productListViewState = _productListViewState.asStateFlow()

    init {
        handleIntent()
    }

    private fun handleIntent() {
        mUiScope.launch {
            productListIntent.consumeAsFlow().collect { intent->
                when (intent){
                    is ProductListIntent.GetProductList -> getProductList()
                }
            }
        }
    }

    private fun getProductList(){
        mUiScope.launch {
            _productListViewState.value = ViewState.Loading
            try {
                val hoodies = mIoScope.async {
                    return@async networkRepository.getHoodieList()
                }.await()

                val sneakers = mIoScope.async {
                    return@async networkRepository.getSneakerList()
                }.await()

                var list = mutableListOf<Product>()
                if (hoodies!=null && hoodies.products.isNotEmpty())
                    list.addAll(hoodies.products)
                if (sneakers!=null && sneakers.products.isNotEmpty())
                    list.addAll(sneakers.products)
                _productListViewState.value = ViewState.Success(list)
            } catch (e: Exception) {
                _productListViewState.value = ViewState.Failure(returnError(e))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        productListIntent.cancel()
    }
}