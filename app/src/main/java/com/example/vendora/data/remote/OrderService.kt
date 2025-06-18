package com.example.vendora.data.remote

import com.example.vendora.domain.model.order.OrderWrapper
import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.domain.model.order.UserOrdersResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {

    @GET("/admin/api/2025-04/orders.json?status=any")
    suspend fun getOrdersByEmail(
        @Header("X-Shopify-Access-Token") token: String,
        @Query("email") customerEmail: String
    ): UserOrdersResponse

    @GET("/admin/api/2025-04/orders/{orderId}.json")
    suspend fun getOrderById(
        @Header("X-Shopify-Access-Token") token: String,
        @Path("orderId") orderId: Long
    ): SingleOrderResponse

    @POST("/admin/api/2025-04/orders.json")
    suspend fun createOrder(
        @Header("X-Shopify-Access-Token") token: String,
        @Body orderWrapper: OrderWrapper
    ): SingleOrderResponse
}