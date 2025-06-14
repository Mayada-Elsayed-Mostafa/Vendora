package com.example.vendora.domain.repo_interfaces

import com.example.vendora.domain.model.product.Product

interface SearchRepository {

    suspend fun searchProducts(query: String): List<Product>

}