package com.example.vendora.data.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.example.vendora.CartCreateMutation
import com.example.vendora.CartLinesAddMutation
import com.example.vendora.CartLinesRemoveMutation
import com.example.vendora.CartLinesUpdateMutation
import com.example.vendora.GetCartQuery
import com.example.vendora.domain.model.address.CountryResponse
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
import com.example.vendora.domain.model.order.OrderPaymentResult
import com.example.vendora.domain.model.order.OrderWrapper
import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.domain.model.order.UserOrdersResponse
import com.example.vendora.domain.model.product.Products
import com.example.vendora.domain.model.product.SingleProduct
import com.example.vendora.type.CartInput
import com.example.vendora.type.CartLineInput
import com.example.vendora.type.CartLineUpdateInput
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val service: ShopifyService,
    private val payMobService: PaymobService,
    private val orderService: OrderService,
    private val currencyApiService: CurrencyApiService,
    private val apolloClient: ApolloClient,
    private val addressService: AddressService
) : RemoteDataSource {

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


    override suspend fun getAuthToken(apiKey: String): AuthTokenResponse {
        return payMobService.getAuthToken(AuthRequest(apiKey))
    }

    override suspend fun createOrder(request: OrderRequest): OrderResponse {
        return payMobService.createOrder(request)
    }

    override suspend fun createShopifyOrder(token: String, orderWrapper: OrderWrapper): SingleOrderResponse {
        return orderService.createOrder(token,orderWrapper)
    }

    override suspend fun getOrderPaymentResult(id: Int, token: String): OrderPaymentResult {
        return payMobService.getOrderPaymentProcessResult(
            id = id,
            token = token
        )
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

    override suspend fun searchProducts(token: String, query: String): Products {
        return service.searchProducts(token, query)
    }

    override suspend fun getOrdersByEmail(token: String, email: String): UserOrdersResponse {
        return orderService.getOrdersByEmail(token,email)
    }

    override suspend fun getOrderById(token: String, orderId: Long): SingleOrderResponse {
        return orderService.getOrderById(token,orderId)
    }

    override suspend fun createCart(): CartCreateMutation.Data {
        val response = apolloClient.mutation(
            CartCreateMutation(
                input = CartInput(
                    lines = Optional.absent(),
                    attributes = Optional.absent()
                )
            )
        ).execute()

        return response.data ?: throw Exception("No data returned from create cart")
    }

    override suspend fun addToCart(
        cartId: String,
        lines: List<CartLineInput>
    ): CartLinesAddMutation.Data {

        val response = apolloClient.mutation(
            CartLinesAddMutation(
                cartId = cartId,
                lines = lines
            )
        ).execute()

        return response.data ?: throw Exception("No data returned from add to cart")
    }


    override suspend fun updateCartLines(
        cartId: String,
        lines: List<CartLineUpdateInput>
    ): CartLinesUpdateMutation.Data {
        val response = apolloClient.mutation(
            CartLinesUpdateMutation(
                cartId = cartId,
                lines = lines
            )
        ).execute()


        return response.data ?: throw Exception("No data returned from update cart")
    }

    override suspend fun removeFromCart(
        cartId: String,
        lineIds: List<String>
    ): CartLinesRemoveMutation.Data {
        val response = apolloClient.mutation(
            CartLinesRemoveMutation(
                cartId = cartId,
                lineIds = lineIds
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception(response.errors?.first()?.message ?: "Failed to remove from cart")
        }

        return response.data ?: throw Exception("No data returned from remove from cart")
    }

    override suspend fun getCart(cartId: String): GetCartQuery.Data {
        val response = apolloClient.query(
            GetCartQuery(id = cartId)
        ).execute()

        if (response.hasErrors()) {
            throw Exception(response.errors?.first()?.message ?: "Failed to get cart")
        }

        return response.data ?: throw Exception("No data returned from get cart")
    }

    override suspend fun getCountryById(token: String, countryId: Long): CountryResponse {
        return addressService.getCountryById(token,countryId)
    }

    companion object{
        const val BASE_URL = "https://mad45-ism-and1.myshopify.com"
    }
}