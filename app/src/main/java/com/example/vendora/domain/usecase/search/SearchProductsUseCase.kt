package com.example.vendora.domain.usecase.search

import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.repo_interfaces.SearchRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(query: String): Flow<Result<List<Product>>> {
        return repository.searchProducts(query)
    }
}