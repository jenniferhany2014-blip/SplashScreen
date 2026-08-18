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

sealed class ProductDetailUiState {
    data object Loading : ProductDetailUiState()

    data class Success(
        val product: Product
    ) : ProductDetailUiState()

    data class Error(
        val error: AppError
    ) : ProductDetailUiState()
}

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)

    val uiState: StateFlow<ProductDetailUiState> =
        _uiState.asStateFlow()

    val favoriteIds: StateFlow<Set<Int>> =
        favoriteRepository.favoriteIds

    fun toggleFavorite(productId: Int) {
        favoriteRepository.toggleFavorite(productId)
    }

    fun fetchProduct(id: Int) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading

            try {
                val product = repository.getProductById(id)

                // Keep detail products available to the Favorites screen.
                repository.saveProducts(listOf(product))

                _uiState.value = ProductDetailUiState.Success(product)
            } catch (e: Exception) {
                _uiState.value = ProductDetailUiState.Error(
                    ErrorMapper.map(e)
                )
            }
        }
    }
}
