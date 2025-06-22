package com.example.vendora.domain.model.order

data class OrderPaymentResult(
    val amount_cents: Int,
    val api_source: String,
    val collector: Any,
    val commission_fees: Int,
    val created_at: String,
    val currency: String,
    val delivery_fees_cents: Int,
    val delivery_needed: Boolean,
    val delivery_vat_cents: Int,
    val id: Int,
    val is_cancel: Boolean,
    val is_canceled: Boolean,
    val is_payment_locked: Boolean,
    val is_return: Boolean,
    val is_returned: Boolean,
    val items: List<Item>,
    val merchant: Merchant,
    val merchant_order_id: Any,
    val merchant_staff_tag: Any,
    val notify_user_with_email: Boolean,
    val order_url: String,
    val paid_amount_cents: Int,
    val payment_method: String,
    val payment_status: String,
    val shipping_data: Any,
    val wallet_notification: Any
)

data class Merchant(
    val city: String,
    val company_emails: List<String>,
    val company_name: String,
    val country: String,
    val created_at: String,
    val id: Int,
    val phones: List<String>,
    val postal_code: String,
    val state: String,
    val street: String
)

data class Item(
    val amount_cents: Int,
    val description: String,
    val name: String,
    val quantity: Int
)