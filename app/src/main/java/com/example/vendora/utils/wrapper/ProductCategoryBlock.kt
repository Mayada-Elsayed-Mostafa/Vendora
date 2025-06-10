package com.example.vendora.utils.wrapper

import com.example.vendora.domain.model.product.Product

data class ProductCategoryBlock(
    val products: List<Product>,
    val categoryTitle: String
)
