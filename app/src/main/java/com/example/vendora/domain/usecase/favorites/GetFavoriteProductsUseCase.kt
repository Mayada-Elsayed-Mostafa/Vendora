package com.example.vendora.domain.usecase.favorites

import com.example.vendora.domain.repo_interfaces.FavoritesRepository
import javax.inject.Inject

class GetFavoriteProductsUseCase @Inject constructor(private val repo: FavoritesRepository) {
    operator fun invoke() = repo.getFavoriteProducts()
}