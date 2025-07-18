package com.example.vendora.data.remote

import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.category.CategoryResponse
import com.example.vendora.domain.model.customer.CreatedCustomerResponse
import com.example.vendora.domain.model.customer.CustomerRequest
import com.example.vendora.domain.model.discount.DiscountCodesResponse
import com.example.vendora.domain.model.product.Products
import com.example.vendora.domain.model.product.SingleProduct
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopifyService {

    @GET("/admin/api/2025-04/smart_collections.json")
    suspend fun getBrands(@Header("X-Shopify-Access-Token") token: String): BrandsResponse

    @GET("/admin/api/2025-04/products.json")
    suspend fun getProductsByBrandId(
        @Query("collection_id") brandId: Long,
        @Header("X-Shopify-Access-Token") token: String
    ): Products

    @POST("/admin/api/2025-04/customers.json")
    suspend fun createCustomer(
        @Header("X-Shopify-Access-Token") token: String,
        @Body customerRequest: CustomerRequest
    ): CreatedCustomerResponse


    @GET("/admin/api/2025-04/products.json")
    suspend fun getProducts(
        @Header("X-Shopify-Access-Token") token: String
    ): Products

    @GET("/admin/api/2025-04/custom_collections.json")
    suspend fun getCategories(
        @Header("X-Shopify-Access-Token") token: String
    ): CategoryResponse

    @GET("/admin/api/2025-04/products/{product_id}.json")
    suspend fun getProductById(
        @Header("X-Shopify-Access-Token") token: String,
        @Path("product_id") productId: Long
    ): SingleProduct

    @GET("/admin/api/2025-04/price_rules/1499001651431/discount_codes.json")
    suspend fun getDiscountCodes(@Header("X-Shopify-Access-Token") token: String): DiscountCodesResponse

    @GET("/admin/api/2025-04/products.json")
    suspend fun searchProducts(
        @Header("X-Shopify-Access-Token") token: String,
        @Query("title") query: String
    ): Products

}