package com.example.splashscreen

import com.example.splashscreen.data.ProductDao
import com.example.splashscreen.data.toEntity
import com.example.splashscreen.data.toProduct
import javax.inject.Inject

/**
 * Repository responsible for product data.
 *
 * Libraries/components:
 * - Retrofit through ProductApiService
 * - Room through ProductDao
 * - Dagger Hilt for dependency injection
 *
 * Responsibility:
 * - Retrieves products from the remote API.
 * - Retrieves cached products from Room.
 * - Saves products to the local database.
 * - Hides the actual data sources from ViewModels.
 */
class ProductRepository @Inject constructor(
    private val api: ProductApiService,
    private val productDao: ProductDao
) {

    /**
     * Retrieves the latest products from the remote API.
     *
     * Retrofit exceptions are allowed to propagate to the caller,
     * where the application's ErrorMapper converts them into
     * AppError values.
     *
     * @return list of products received from the server.
     */
    suspend fun getProducts(): List<Product> {
        return api.getProducts().products
    }

    /**
     * Retrieves products stored locally in Room.
     *
     * @return cached products, or an empty list when no products
     * are stored.
     */
    suspend fun getCachedProducts(): List<Product> {
        return productDao
            .getAll()
            .map { it.toProduct() }
    }

    /**
     * Saves products to the local Room database.
     *
     * @param products products that should be cached locally.
     */
    suspend fun saveProducts(
        products: List<Product>
    ) {
        productDao.insertAll(
            products.map { it.toEntity() }
        )
    }

    /**
     * Retrieves a single product from the remote API.
     *
     * @param id product identifier.
     * @return requested product.
     */
    suspend fun getProductById(
        id: Int
    ): Product {
        return api.getProductById(id)
    }

    /**
     * Searches products using the remote API.
     *
     * @param query text entered by the user.
     * @return products matching the query.
     */
    suspend fun searchProducts(
        query: String
    ): List<Product> {
        return api.searchProducts(query).products
    }
}