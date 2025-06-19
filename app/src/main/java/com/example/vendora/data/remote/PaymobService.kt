package com.example.vendora.data.remote

import com.example.vendora.domain.model.order.OrderPaymentResult
import com.example.vendora.domain.model.payment.AuthRequest
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymobService {
    @POST("auth/tokens")
    suspend fun getAuthToken(@Body request: AuthRequest):AuthTokenResponse

    @POST("ecommerce/orders")
    suspend fun createOrder(@Body request: OrderRequest): OrderResponse

    @POST("acceptance/payment_keys")
    suspend fun getPaymentKey(@Body request: PaymentKeyRequest): PaymentKeyResponse

    @GET("/ecommerce/orders/{orderId}")
    suspend fun getOrderPaymentProcessResult(
        @Path("orderId") id: Int,
        @Header("Authorization") token: String
    ): OrderPaymentResult
}