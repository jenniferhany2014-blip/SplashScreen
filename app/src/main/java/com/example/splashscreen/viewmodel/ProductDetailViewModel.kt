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
 * Represents the state of the product details screen.
 */
sealed class ProductDetailUiState {

    /**
     * Product details are loading.
     */
    data object Loading : ProductDetailUiState()

    /**
     * Product details loaded successfully.
     *
     * @param product loaded product.
     */
    data class Success(
        val product: Product
    ) : ProductDetailUiState()

    /**
     * Product details failed to load.
     *
     * @param error application-level error.
     */
    data class Error(
        val error: AppError
    ) : ProductDetailUiState()
}

/**
 * ViewModel responsible for loading a single product.
 *
 * Libraries:
 * - AndroidX Lifecycle ViewModel
 * - Kotlin Coroutines
 * - StateFlow
 * - Dagger Hilt
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ProductDetailUiState>(
            ProductDetailUiState.Loading
        )

    val uiState: StateFlow<ProductDetailUiState> =
        _uiState

    /**
     * Loads a product by its identifier.
     *
     * @param id product identifier.
     */
    fun fetchProduct(
        id: Int
    ) {

        viewModelScope.launch {

            _uiState.value =
                ProductDetailUiState.Loading

            try {

                val product =
                    repository.getProductById(id)

                _uiState.value =
                    ProductDetailUiState.Success(
                        product
                    )

            } catch (e: Exception) {

                _uiState.value =
                    ProductDetailUiState.Error(
                        ErrorMapper.map(e)
                    )
            }
        }
    }
}