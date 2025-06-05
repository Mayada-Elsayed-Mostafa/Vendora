package com.example.vendora.data.remote

import com.example.vendora.domain.model.brands.BrandsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceImpl: RemoteDataSource {

    private val baseUrl = "https://mad45-ism-and1.myshopify.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService: ShopifyService by lazy {
        retrofit.create(ShopifyService::class.java)
    }

    override suspend fun getBrands(token: String): BrandsResponse {
        return retrofitService.getBrands(token)
    }
}