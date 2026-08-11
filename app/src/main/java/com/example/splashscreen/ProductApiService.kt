package com.example.splashscreen

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
interface ProductApiService {

    @GET("products")
    suspend fun getProducts(): ProductListResponse

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product


    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String): ProductListResponse
}