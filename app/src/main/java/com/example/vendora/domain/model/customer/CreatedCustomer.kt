package com.example.vendora.domain.model.customer

import kotlinx.serialization.Serializable

@Serializable
data class CreatedCustomerResponse(
    val customer: CreatedCustomer
)

@Serializable
data class CreatedCustomer(
    val id: Long,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String
)