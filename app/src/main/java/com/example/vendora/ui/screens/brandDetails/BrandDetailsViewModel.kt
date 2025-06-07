package com.example.vendora.ui.screens.brandDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.usecase.products.GetProductsByBrandIdUseCase
import com.example.vendora.utils.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrandDetailsViewModel @Inject constructor(private val useCase: GetProductsByBrandIdUseCase) :
    ViewModel() {

    private var _uiState = MutableStateFlow(BrandDetailsUIState())
    val uiState = _uiState.asStateFlow()

    fun getProductsByBrandId(id: Long) {
        viewModelScope.launch {
            useCase.invoke(id)
                .flowOn(Dispatchers.IO)
                .map { item ->
                    when (item) {
                        is Result.Success -> Result.Success(item.data.products)
                        is Result.Failure -> Result.Failure(item.exception)
                        is Result.Loading -> Result.Loading
                    }
                }.collect { result ->
                    _uiState.update { state ->
                        val filtered = if (result is Result.Success) result.data else emptyList()
                        state.copy(
                            products = result,
                            filteredProducts = filtered
                        )
                    }
                }
        }
    }

    fun filterProducts(query: String) {
        _uiState.update {
            val allProducts = (it.products as Result.Success).data
            it.copy(
                filteredProducts = allProducts
                    .filter { product ->
                        product.title.lowercase().contains(query)
                    }
            )
        }
        Log.d("BrandScreen", _uiState.value.filteredProducts.size.toString())
    }
}

data class BrandDetailsUIState(
    val searchQuery: String = "",
    val products: Result<List<Product>> = Result.Loading,
    val filteredProducts: List<Product> = emptyList()
)