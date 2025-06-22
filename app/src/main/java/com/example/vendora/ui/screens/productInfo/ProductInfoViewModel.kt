package com.example.vendora.ui.screens.productInfo

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.usecase.products.GetProductByIdUseCase
import com.example.vendora.utils.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductInfoViewModel @Inject constructor(
    private val getProductUseCase: GetProductByIdUseCase
) : ViewModel() {
    private var _product = MutableStateFlow<Result<Product>>(Result.Loading)
    val product = _product.asStateFlow()

    private val _selectedColor = mutableStateOf("")
    val selectedColor: State<String> = _selectedColor

    private val _selectedSize = mutableStateOf("")
    val selectedSize: State<String> = _selectedSize

    private val _quantity = mutableIntStateOf(1)
    val quantity: State<Int> = _quantity

    private val _isFavorite = mutableStateOf(false)
    val isFavorite: State<Boolean> = _isFavorite

    fun initializeOptions(colors: List<String>, sizes: List<String>) {
        if (_selectedColor.value.isEmpty() && colors.isNotEmpty()) {
            _selectedColor.value = colors.first()
        }
        if (_selectedSize.value.isEmpty() && sizes.isNotEmpty()) {
            _selectedSize.value = sizes.first()
        }
    }

    fun onColorSelected(color: String) {
        _selectedColor.value = color
    }

    fun onSizeSelected(size: String) {
        _selectedSize.value = size
    }

    fun increaseQuantity() {
        _quantity.value += 1
    }

    fun decreaseQuantity() {
        if (_quantity.value > 1) _quantity.value -= 1
    }

    fun loadProduct(productId: Long) {
        viewModelScope.launch {
            _product.value = Result.Loading
            getProductUseCase(productId).collect { result ->
                _product.value = result
            }
        }
    }

}