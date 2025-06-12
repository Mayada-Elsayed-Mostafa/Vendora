package com.example.vendora.domain.model.payment

data class OrderRequest(
    val auth_token: String,
    val amount_cents: Int,
    val delivery_needed: Boolean = false,
    val currency: String = "EGP",
    val items: List<Any> = listOf()
    )
