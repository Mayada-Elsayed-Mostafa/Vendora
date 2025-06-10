package com.example.vendora.domain.model.customer

import kotlinx.serialization.Serializable

@Serializable
data class CustomerRequest(
    val customer: Customer
)

@Serializable
data class Customer(
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
)