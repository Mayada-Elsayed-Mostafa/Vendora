package com.example.vendora.data.remote

import com.example.vendora.domain.model.brands.BrandsResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface ShopifyService {

    @GET("/admin/api/2025-04/smart_collections.json")
    suspend fun getBrands(@Header("X-Shopify-Access-Token") token: String): BrandsResponse
}