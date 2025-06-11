package com.example.vendora.data.remote

import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.discount.DiscountCodesResponse
import com.example.vendora.domain.model.product.Products
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ShopifyService {

    @GET("/admin/api/2025-04/smart_collections.json")
    suspend fun getBrands(@Header("X-Shopify-Access-Token") token: String): BrandsResponse

    @GET("/admin/api/2025-04/products.json")
    suspend fun getProductsByBrandId(
        @Query("collection_id") brandId: Long,
        @Header("X-Shopify-Access-Token") token: String
    ): Products

    @GET("/admin/api/2025-04/price_rules/1499001651431/discount_codes.json")
    suspend fun getDiscountCodes(@Header("X-Shopify-Access-Token") token: String) : DiscountCodesResponse
}