package com.example.vendora.data.remote

import com.example.vendora.BuildConfig
import com.example.vendora.domain.model.brands.BrandsResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface ShopifyService {
    @Headers("X-Shopify-Access-Token: ${BuildConfig.adminApiAccessToken}")
    @GET("/admin/api/2025-04/smart_collections.json")
    suspend fun getBrands(): BrandsResponse
}