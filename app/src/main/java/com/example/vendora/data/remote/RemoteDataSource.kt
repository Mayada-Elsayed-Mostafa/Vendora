package com.example.vendora.data.remote

import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.customer.CreatedCustomerResponse
import com.example.vendora.domain.model.customer.CustomerRequest
import com.example.vendora.domain.model.product.Products

interface RemoteDataSource {
    suspend fun getBrands(token: String): BrandsResponse

    suspend fun getProductsByBrandId(brandId: Long, token: String): Products

    suspend fun createCustomer(token: String, request: CustomerRequest): CreatedCustomerResponse

}