package com.example.vendora.domain.model.payment

data class PaymentKeyRequest(
    val auth_token: String,
    val amount_cents: Int,
    val expiration: Int = 3600,
    val order_id: Int,
    val billing_data: BillingData,
    val currency: String = "EGP",
    val integration_id: Int
)
