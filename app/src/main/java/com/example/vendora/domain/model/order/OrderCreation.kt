package com.example.vendora.domain.model.order

data class OrderWrapper(
    val order: OrderBuild
)

data class OrderBuild(
    val currency: String,
    val discount_codes: List<DiscountCodeBuild>,
    val email: String,
    val financial_status: String?,
    val fulfillment_status: String?,
    val line_items: List<LineItemBuild>,
    val send_receipt: Boolean,
    val shipping_address: ShippingAddressBuild
)

data class DiscountCodeBuild(
    val amount: Double,
    val code: String,
    val type: String
)

data class ShippingAddressBuild(
    val address: String,
    val city: String,
    val country: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val province: String,
)

data class LineItemBuild(
    val price: Double,
    val product_id: Long? = null,
    val quantity: Int,
    val title: String
)