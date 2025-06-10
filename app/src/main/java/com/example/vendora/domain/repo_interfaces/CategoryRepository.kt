package com.example.vendora.domain.repo_interfaces

import com.example.vendora.domain.model.product.Product
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun getCategories()

    suspend fun categorizedProductsFactory(title: String)
}