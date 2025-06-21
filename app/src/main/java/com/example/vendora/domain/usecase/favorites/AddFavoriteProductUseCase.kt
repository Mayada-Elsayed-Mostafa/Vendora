package com.example.vendora.domain.usecase.favorites

import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.repo_interfaces.FavoritesRepository
import javax.inject.Inject

class AddFavoriteProductUseCase @Inject constructor(private val repo: FavoritesRepository) {
    suspend operator fun invoke(product: Product) = repo.addFavoriteProduct(product)
}