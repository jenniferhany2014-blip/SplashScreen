package com.example.splashscreen

class ProductRepository(
    private val api: ProductApiService = RetrofitInstance.api
) {
    suspend fun getProducts(): List<Product> = api.getProducts().products

    suspend fun getProductById(id: Int): Product = api.getProductById(id)
    suspend fun searchProducts(query: String): List<Product> = api.searchProducts(query).products
}