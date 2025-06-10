package com.example.vendora.data.remote

import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.category.CategoryResponse
import com.example.vendora.domain.model.product.Products
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val service: ShopifyService) : RemoteDataSource {

    override suspend fun getBrands(token: String): BrandsResponse {
        return service.getBrands(token)
    }

    override suspend fun getProductsByBrandId(brandId: Long, token: String): Products {
        return service.getProductsByBrandId(brandId,token)
    }

    override suspend fun getProducts(token: String): Products {
        return service.getProducts(token)
    }

    override suspend fun getCategories(token: String): CategoryResponse {
        return service.getCategories(token)
    }

    companion object{
        const val BASE_URL = "https://mad45-ism-and1.myshopify.com"
    }
}