package com.example.vendora.data.remote

import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import com.example.vendora.domain.model.product.Products

interface RemoteDataSource {
    suspend fun getBrands(token: String): BrandsResponse

    suspend fun getProductsByBrandId(brandId: Long, token: String): Products


    ///PayMob
    suspend fun getAuthToken(apiKey: String):AuthTokenResponse

    suspend fun  createOrder(request: OrderRequest) : OrderResponse

    suspend fun getPaymentKey(request: PaymentKeyRequest) : PaymentKeyResponse
}