package com.example.vendora.domain.repo_interfaces

import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow

interface PaymobRepository {
    fun getToken(): Flow<Result<AuthTokenResponse>>

    fun createOrder (orderRequest: OrderRequest): Flow<Result<OrderResponse>>

    fun getPaymentKey (paymentKeyRequest: PaymentKeyRequest) : Flow<Result<PaymentKeyResponse>>
}