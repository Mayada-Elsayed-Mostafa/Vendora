package com.example.vendora.data.remote

import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.payment.AuthRequest
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import com.example.vendora.domain.model.product.Products
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val service: ShopifyService , private val payMobService: PaymobService) : RemoteDataSource {

//    private val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl(baseUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()

//    private val retrofitService: ShopifyService by lazy {
//        retrofit.create(ShopifyService::class.java)
//    }

    override suspend fun getBrands(token: String): BrandsResponse {
        return service.getBrands(token)
    }

    override suspend fun getProductsByBrandId(brandId: Long, token: String): Products {
        return service.getProductsByBrandId(brandId,token)
    }

    override suspend fun getAuthToken(apiKey: String): AuthTokenResponse {
        return payMobService.getAuthToken(AuthRequest(apiKey))
    }

    override suspend fun createOrder(request: OrderRequest): OrderResponse {
        return payMobService.createOrder(request)
    }

    override suspend fun getPaymentKey(request: PaymentKeyRequest): PaymentKeyResponse {
        return payMobService.getPaymentKey(request)
    }

    companion object{
        const val BASE_URL = "https://mad45-ism-and1.myshopify.com"
    }
}