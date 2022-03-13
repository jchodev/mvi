package com.jerry.mvi.data.repository

import com.jerry.mvi.data.api.ProductApi
import com.jerry.mvi.data.model.ProductListResponse
import javax.inject.Inject

class NetworkRepository @Inject constructor(
        private val productApi : ProductApi
) {
    suspend fun getHoodieList(): ProductListResponse {
        return  productApi.getHoodieList()
    }

    suspend fun getSneakerList(): ProductListResponse {
        return  productApi.getSneakerList()
    }
}