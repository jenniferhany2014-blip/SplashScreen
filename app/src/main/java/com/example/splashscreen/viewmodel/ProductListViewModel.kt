package com.example.splashscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splashscreen.FavoriteRepository
import com.example.splashscreen.Product
import com.example.splashscreen.ProductRepository
import com.example.splashscreen.error.AppError
import com.example.splashscreen.error.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProductListUiState {
    data object Loading : ProductListUiState()

    data class Success(
        val products: List<Product>,
        val fromCache: Boolean = false
    ) : ProductListUiState()

    data class Error(
        val error: AppError,
        val cachedProducts: List<Product> = emptyList()
    ) : ProductListUiState()
}

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)

    val uiState: StateFlow<ProductListUiState> =
        _uiState.asStateFlow()

    val favoriteIds: StateFlow<Set<Int>> =
        favoriteRepository.favoriteIds

    fun toggleFavorite(productId: Int) {
        favoriteRepository.toggleFavorite(productId)
    }

    fun fetchProducts() {
        viewModelScope.launch {
            val cachedProducts = runCatching {
                repository.getCachedProducts()
            }.getOrDefault(emptyList())

            if (cachedProducts.isNotEmpty()) {
                _uiState.value = ProductListUiState.Success(
                    products = cachedProducts,
                    fromCache = true
                )
            } else {
                _uiState.value = ProductListUiState.Loading
            }

            try {
                val freshProducts = repository.getProducts()

                if (freshProducts.isEmpty()) {
                    throw IllegalStateException("The server returned no products.")
                }

                repository.saveProducts(freshProducts)

                _uiState.value = ProductListUiState.Success(
                    products = freshProducts,
                    fromCache = false
                )
            } catch (e: Exception) {
                val error = ErrorMapper.map(e)

                if (cachedProducts.isNotEmpty()) {
                    _uiState.value = ProductListUiState.Success(
                        products = cachedProducts,
                        fromCache = true
                    )
                } else {
                    _uiState.value = ProductListUiState.Error(error = error)
                }
            }
        }
    }

    fun fetchFavorites() {
        viewModelScope.launch {
            _uiState.value = ProductListUiState.Loading

            try {
                var products = repository.getCachedProducts()

                if (products.isEmpty()) {
                    val freshProducts = repository.getProducts()
                    if (freshProducts.isNotEmpty()) {
                        repository.saveProducts(freshProducts)
                        products = freshProducts
                    }
                }

                _uiState.value = ProductListUiState.Success(
                    products = products,
                    fromCache = true
                )
            } catch (e: Exception) {
                _uiState.value = ProductListUiState.Error(
                    error = ErrorMapper.map(e)
                )
            }
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            fetchProducts()
            return
        }

        viewModelScope.launch {
            _uiState.value = ProductListUiState.Loading

            try {
                val products = repository.searchProducts(query)
                _uiState.value = ProductListUiState.Success(products = products)
            } catch (e: Exception) {
                _uiState.value = ProductListUiState.Error(
                    error = ErrorMapper.map(e)
                )
            }
        }
    }
}
