package com.example.splashscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splashscreen.Product
import com.example.splashscreen.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProductListUiState {
    data object Loading : ProductListUiState()
    data class Success(val products: List<Product>) : ProductListUiState()
    data class Error(val message: String) : ProductListUiState()
}

class ProductListViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState: StateFlow<ProductListUiState> = _uiState

    fun fetchProducts() {
        viewModelScope.launch {
            _uiState.value = ProductListUiState.Loading
            try {
                val products = repository.getProducts()
                _uiState.value = ProductListUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = ProductListUiState.Error(e.localizedMessage ?: "Something went wrong")
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            _uiState.value = ProductListUiState.Loading
            try {
                val products = repository.searchProducts(query)
                _uiState.value = ProductListUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = ProductListUiState.Error(e.localizedMessage ?: "Something went wrong")
            }
        }
    }
}