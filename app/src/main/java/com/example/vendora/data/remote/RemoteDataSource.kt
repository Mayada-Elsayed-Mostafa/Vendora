package com.example.vendora.data.remote

import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.category.CategoryResponse
import com.example.vendora.domain.model.customer.CreatedCustomerResponse
import com.example.vendora.domain.model.customer.CustomerRequest
import com.example.vendora.domain.model.product.Products
import com.example.vendora.domain.model.product.SingleProduct

interface RemoteDataSource {
    suspend fun getBrands(token: String): BrandsResponse

    suspend fun getProductsByBrandId(brandId: Long, token: String): Products

    suspend fun createCustomer(
        token: String,
        request: CustomerRequest
    ): CreatedCustomerResponse

    suspend fun getProducts(token: String): Products

    suspend fun getCategories(token: String): CategoryResponse

    suspend fun getProductById(token: String, productId: Long): SingleProduct
}