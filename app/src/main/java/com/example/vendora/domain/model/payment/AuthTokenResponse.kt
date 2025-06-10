package com.example.vendora.domain.model.payment

data class AuthRequest(
    val api_key: String
)

data class AuthTokenResponse (val token: String)