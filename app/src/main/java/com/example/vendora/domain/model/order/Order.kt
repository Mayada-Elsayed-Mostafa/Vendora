package com.example.vendora.domain.model.order

import java.util.UUID

data class UserOrdersResponse(
    val orders: List<Order>
)

data class SingleOrderResponse(
    val order: Order
)

data class Order(
    val id: Long,
    val confirmation_number: String,
    val created_at: String,
    val currency: String,
    val email: String,
    val financial_status: String,
    val total_price: String,
    val total_line_items_price: String,
    val total_discounts: String,
    val discount_codes: List<DiscountCode>,
    val customer: Customer,
    val line_items: List<LineItem>
)

data class DiscountCode(
    val code: String,
    val amount: String,
    val type: String
)

data class Customer(
    val id: Long,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone: String?,
    val default_address: Address?
)

data class Address(
    val id: Long,
    val first_name: String,
    val last_name: String,
    val address1: String,
    val address2: String?,
    val city: String,
    val province: String,
    val country: String,
    val zip: String,
    val phone: String?,
    val country_code: String,
    val province_code: String
)

data class LineItem(
    val id: Long = UUID.randomUUID().leastSignificantBits,
    val name: String,
    val price: String,
    val quantity: Int,
    val title: String,
    val total_discount: String = ""
)