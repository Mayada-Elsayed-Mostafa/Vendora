package com.example.vendora.domain.model.currency

data class CurrencyInfo(
    val code: String,
    val value: Double
)

data class CurrencyResponse(
    val data: Map<String, CurrencyInfo>
)
