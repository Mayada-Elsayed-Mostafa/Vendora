package com.example.vendora.data.remote

import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.customer.CreatedCustomerResponse
import com.example.vendora.domain.model.customer.CustomerRequest
import com.example.vendora.domain.model.product.Products
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val service: ShopifyService) :
    RemoteDataSource {

//    private val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl(baseUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()

//    private val retrofitService: ShopifyService by lazy {
//        retrofit.create(ShopifyService::class.java)
//    }

    override suspend fun getBrands(token: String): BrandsResponse {
        return service.getBrands(token)
    }

    override suspend fun getProductsByBrandId(
        brandId: Long, token: String
    ): Products {
        return service.getProductsByBrandId(brandId, token)
    }

    override suspend fun createCustomer(
        token: String,
        request: CustomerRequest
    ): CreatedCustomerResponse {
        return service.createCustomer(token, request)
    }

    companion object {
        const val BASE_URL = "https://mad45-ism-and1.myshopify.com"
    }
}