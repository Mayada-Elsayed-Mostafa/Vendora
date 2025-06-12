package com.example.vendora.domain.repo_interfaces

import com.example.vendora.domain.model.product.Product
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getProductById(brandId: Long): Flow<Result<Product>>

}