package com.example.vendora.domain.model.product

data class Option(
    val id: Long = 0L,
    val name: String = "",
    val position: Int = 0,
    val product_id: Long = 0L,
    val values: List<String> = emptyList()
)
