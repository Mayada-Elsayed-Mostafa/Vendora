package com.example.vendora.domain.repo_interfaces

import com.example.vendora.domain.model.product.Product
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun addFavoriteProduct(product: Product)
    fun getFavoriteProducts(): Flow<List<Product>>
    suspend fun removeFavoriteProduct(productId: Long)

}