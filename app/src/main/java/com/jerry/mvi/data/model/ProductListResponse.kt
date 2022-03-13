package com.jerry.mvi.data.model

class ProductListResponse(
    val products : List<Product> = emptyList(),
    val title : String?,
    val product_count : Int?,
)