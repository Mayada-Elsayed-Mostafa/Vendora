package com.example.vendora.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.usecase.favorites.AddFavoriteProductUseCase
import com.example.vendora.domain.usecase.favorites.GetFavoriteProductsUseCase
import com.example.vendora.domain.usecase.favorites.RemoveFavoriteProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavorites: GetFavoriteProductsUseCase,
    private val addFavorite: AddFavoriteProductUseCase,
    private val removeFavorite: RemoveFavoriteProductUseCase
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Product>>(emptyList())
    val favorites: StateFlow<List<Product>> = _favorites.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            getFavorites().collectLatest {
                _favorites.value = it
            }
        }
    }

    fun addToFavorites(product: Product) {
        viewModelScope.launch {
            addFavorite(product)
        }
    }

    fun removeFromFavorites(productId: Long) {
        viewModelScope.launch {
            removeFavorite(productId)
        }
    }

    fun isFavorite(productId: Long): Boolean {
        return _favorites.value.any { it.id == productId }
    }
}
