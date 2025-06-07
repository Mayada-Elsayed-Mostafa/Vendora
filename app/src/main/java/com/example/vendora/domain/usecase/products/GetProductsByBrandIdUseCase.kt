package com.example.vendora.domain.usecase.products

import com.example.vendora.domain.model.product.Products
import com.example.vendora.domain.repo_interfaces.ProductsRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsByBrandIdUseCase @Inject constructor(private val repository: ProductsRepository) {
    operator fun invoke(brandId: Long): Flow<Result<Products>> {
        return repository.getProductsByBrandId(brandId)
    }
}