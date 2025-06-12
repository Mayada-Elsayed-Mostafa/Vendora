package com.example.vendora.domain.usecase.products

import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.repo_interfaces.ProductRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(private val repository: ProductRepository) {
    operator fun invoke(productId: Long): Flow<Result<Product>> {
        return repository.getProductById(productId)
    }
}