package com.example.vendora.domain.usecase.favorites

import com.example.vendora.domain.repo_interfaces.FavoritesRepository
import javax.inject.Inject

class RemoveFavoriteProductUseCase @Inject constructor(private val repo: FavoritesRepository) {
    suspend operator fun invoke(productId: Long) = repo.removeFavoriteProduct(productId)
}