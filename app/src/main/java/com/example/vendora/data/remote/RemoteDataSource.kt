package com.example.vendora.data.remote

import com.example.vendora.CartCreateMutation
import com.example.vendora.CartLinesAddMutation
import com.example.vendora.CartLinesRemoveMutation
import com.example.vendora.CartLinesUpdateMutation
import com.example.vendora.GetCartQuery
import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.category.CategoryResponse
import com.example.vendora.domain.model.customer.CreatedCustomerResponse
import com.example.vendora.domain.model.customer.CustomerRequest
import com.example.vendora.domain.model.currency.CurrencyResponse
import com.example.vendora.domain.model.discount.DiscountCode
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import com.example.vendora.domain.model.product.Products
import com.example.vendora.domain.model.product.SingleProduct
import com.example.vendora.type.CartLineInput
import com.example.vendora.type.CartLineUpdateInput

interface RemoteDataSource {
    suspend fun getBrands(token: String): BrandsResponse

    suspend fun getProductsByBrandId(brandId: Long, token: String): Products

    suspend fun createCustomer(
        token: String,
        request: CustomerRequest
    ): CreatedCustomerResponse

    suspend fun getProducts(token: String): Products

    suspend fun getCategories(token: String): CategoryResponse

    suspend fun getProductById(token: String, productId: Long): SingleProduct


    ///PayMob
    suspend fun getAuthToken(apiKey: String):AuthTokenResponse

    suspend fun  createOrder(request: OrderRequest) : OrderResponse

    suspend fun getPaymentKey(request: PaymentKeyRequest) : PaymentKeyResponse

    //Discount code
    suspend fun getDiscountCodes(token: String):List<DiscountCode>

    //currency
    suspend fun getCurrency(apiKey: String,baseCurrency: String) : CurrencyResponse



    /// cart Methods

    suspend fun createCart(): CartCreateMutation.Data

    suspend fun addToCart(cartId: String, lines: List<CartLineInput>): CartLinesAddMutation.Data

    suspend fun updateCartLines(cartId: String, lines: List<CartLineUpdateInput>): CartLinesUpdateMutation.Data

    suspend fun removeFromCart(cartId: String, lineIds: List<String>) : CartLinesRemoveMutation.Data

    suspend fun getCart(cartId: String): GetCartQuery.Data
}