package com.jerry.mvi.data.api

import com.jerry.mvi.data.model.ProductListResponse
import retrofit2.http.GET

interface ProductApi {

    @GET("/media/catalog/android_test_example.json")
    suspend fun getHoodieList(): ProductListResponse

    @GET("/media/catalog/example.json")
    suspend fun getSneakerList(): ProductListResponse

}