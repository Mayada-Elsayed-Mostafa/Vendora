package com.example.vendora.domain.model.brands

data class Rule(
    val column: String,
    val condition: String,
    val relation: String
)