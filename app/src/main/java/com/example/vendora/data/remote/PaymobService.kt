package com.example.vendora.data.remote

import com.example.vendora.domain.model.payment.AuthRequest
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymobService {
    @POST("auth/tokens")
    suspend fun getAuthToken(@Body request: AuthRequest):AuthTokenResponse

    @POST("ecommerce/orders")
    suspend fun createOrder(@Body request: OrderRequest): OrderResponse

    @POST("acceptance/payment_keys")
    suspend fun getPaymentKey(@Body request: PaymentKeyRequest): PaymentKeyResponse
}