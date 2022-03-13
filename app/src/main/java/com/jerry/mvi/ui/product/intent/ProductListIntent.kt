package com.jerry.mvi.ui.product.intent

sealed class ProductListIntent {
    object GetProductList : ProductListIntent()
}