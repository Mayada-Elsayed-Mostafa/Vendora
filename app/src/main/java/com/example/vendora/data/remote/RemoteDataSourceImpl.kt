package com.example.vendora.data.remote

import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.currency.CurrencyResponse
import com.example.vendora.domain.model.discount.DiscountCode
import com.example.vendora.domain.model.payment.AuthRequest
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import com.example.vendora.domain.model.category.CategoryResponse
import com.example.vendora.domain.model.customer.CreatedCustomerResponse
import com.example.vendora.domain.model.customer.CustomerRequest
import com.example.vendora.domain.model.product.Products
import com.example.vendora.domain.model.product.SingleProduct
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val service: ShopifyService , private val payMobService: PaymobService, private val currencyApiService: CurrencyApiService) : RemoteDataSource {

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

    override suspend fun getProductsByBrandId(
        brandId: Long, token: String
    ): Products {
        return service.getProductsByBrandId(brandId, token)
    }

    override suspend fun createCustomer(
        token: String,
        request: CustomerRequest
    ): CreatedCustomerResponse {
        return service.createCustomer(token, request)
    }

    override suspend fun getProducts(token: String): Products {
        return service.getProducts(token)
    }

    override suspend fun getCategories(token: String): CategoryResponse {
        return service.getCategories(token)
    }

    override suspend fun getProductById(token: String, productId: Long): SingleProduct {
        return service.getProductById(token, productId)
    }

    companion object {
    override suspend fun getAuthToken(apiKey: String): AuthTokenResponse {
        return payMobService.getAuthToken(AuthRequest(apiKey))
    }

    override suspend fun createOrder(request: OrderRequest): OrderResponse {
        return payMobService.createOrder(request)
    }

    override suspend fun getPaymentKey(request: PaymentKeyRequest): PaymentKeyResponse {
        return payMobService.getPaymentKey(request)
    }

    override suspend fun getDiscountCodes(token: String): List<DiscountCode> {
        return service.getDiscountCodes(token).discount_codes
    }

    override suspend fun getCurrency(apiKey: String, baseCurrency: String): CurrencyResponse {
        return currencyApiService.getCurrency(apiKey,baseCurrency)
    }

    companion object{
        const val BASE_URL = "https://mad45-ism-and1.myshopify.com"
    }
}