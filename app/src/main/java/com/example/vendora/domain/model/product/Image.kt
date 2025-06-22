package com.example.vendora.domain.model.product

data class Image(
    val admin_graphql_api_id: String = "",
    val alt: String = "",
    val created_at: String = "",
    val height: Int = 0,
    val id: Long = 0L,
    val position: Int = 0,
    val product_id: Long = 0L,
    val src: String = "",
    val updated_at: String = "",
    val variant_ids: List<Any> = emptyList(),
    val width: Int = 0
)
