package com.example.vendora.domain.repo_interfaces

import com.example.vendora.domain.model.brands.BrandsResponse
import kotlinx.coroutines.flow.Flow
import com.example.vendora.utils.wrapper.Result

interface ProductsRepository {

    fun getBrands(): Flow<Result<BrandsResponse>>
}