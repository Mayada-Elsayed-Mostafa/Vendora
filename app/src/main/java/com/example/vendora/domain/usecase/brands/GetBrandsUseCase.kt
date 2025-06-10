package com.example.vendora.domain.usecase.brands

import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.repo_interfaces.ProductsRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetBrandsUseCase @Inject constructor(private val productsRepository: ProductsRepository) {
    operator fun invoke(): Flow<Result<BrandsResponse>>{
        return productsRepository.getBrands()
    }
}