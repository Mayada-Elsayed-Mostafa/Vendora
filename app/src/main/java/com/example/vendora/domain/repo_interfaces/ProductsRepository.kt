package com.example.vendora.domain.repo_interfaces

import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.category.CategoryResponse
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.model.product.Products
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    fun getBrands(): Flow<Result<BrandsResponse>>

    fun getProductsByBrandId(brandId: Long): Flow<Result<Products>>

    fun getProducts(): Flow<Result<Products>>

    fun cacheBrands(response: BrandsResponse)

    fun cacheProducts(response: Products)
}