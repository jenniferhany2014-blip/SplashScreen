package com.example.splashscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splashscreen.Product
import com.example.splashscreen.ProductRepository
import com.example.splashscreen.error.AppError
import com.example.splashscreen.error.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Represents the state displayed by the product list screen.
 */
sealed class ProductListUiState {

    /**
     * Products are currently being loaded.
     */
    data object Loading : ProductListUiState()

    /**
     * Products were loaded successfully.
     *
     * @param products products to display.
     * @param fromCache true when products came from Room cache.
     */
    data class Success(
        val products: List<Product>,
        val fromCache: Boolean = false
    ) : ProductListUiState()

    /**
     * Loading failed.
     *
     * @param error application-level error.
     * @param cachedProducts cached products available after failure.
     */
    data class Error(
        val error: AppError,
        val cachedProducts: List<Product> = emptyList()
    ) : ProductListUiState()
}

/**
 * ViewModel responsible for product loading and searching.
 *
 * Libraries:
 * - AndroidX Lifecycle ViewModel
 * - Kotlin Coroutines
 * - Kotlin StateFlow
 * - Dagger Hilt
 *
 * Responsibility:
 * - Requests products from ProductRepository.
 * - Displays cached products when network requests fail.
 * - Exposes loading/success/error states.
 * - Converts exceptions into AppError.
 */
@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ProductListUiState>(
            ProductListUiState.Loading
        )

    val uiState: StateFlow<ProductListUiState> =
        _uiState

    /**
     * Loads products from the local cache and remote API.
     *
     * Cached products are shown immediately when available.
     * The remote API is then requested to refresh the data.
     *
     * If the network fails, cached data remains visible.
     */
    fun fetchProducts() {

        viewModelScope.launch {

            val cachedProducts =
                runCatching {
                    repository.getCachedProducts()
                }.getOrDefault(emptyList())

            if (cachedProducts.isNotEmpty()) {

                _uiState.value =
                    ProductListUiState.Success(
                        products = cachedProducts,
                        fromCache = true
                    )

            } else {

                _uiState.value =
                    ProductListUiState.Loading
            }

            try {

                val freshProducts =
                    repository.getProducts()

                if (freshProducts.isEmpty()) {
                    throw IllegalStateException(
                        "The server returned no products."
                    )
                }

                repository.saveProducts(
                    freshProducts
                )

                _uiState.value =
                    ProductListUiState.Success(
                        products = freshProducts,
                        fromCache = false
                    )

            } catch (e: Exception) {

                val error =
                    ErrorMapper.map(e)

                if (cachedProducts.isNotEmpty()) {

                    _uiState.value =
                        ProductListUiState.Success(
                            products = cachedProducts,
                            fromCache = true
                        )

                } else {

                    _uiState.value =
                        ProductListUiState.Error(
                            error = error
                        )
                }
            }
        }
    }

    /**
     * Searches the remote product API.
     *
     * @param query text entered by the user.
     */
    fun search(
        query: String
    ) {

        if (query.isBlank()) {
            fetchProducts()
            return
        }

        viewModelScope.launch {

            _uiState.value =
                ProductListUiState.Loading

            try {

                val products =
                    repository.searchProducts(query)

                _uiState.value =
                    ProductListUiState.Success(
                        products = products
                    )

            } catch (e: Exception) {

                _uiState.value =
                    ProductListUiState.Error(
                        error = ErrorMapper.map(e)
                    )
            }
        }
    }
}