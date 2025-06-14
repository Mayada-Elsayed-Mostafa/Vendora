package com.example.vendora.domain.usecase.search

import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.repo_interfaces.SearchRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String): List<Product> {
        return repository.searchProducts(query)
    }
}