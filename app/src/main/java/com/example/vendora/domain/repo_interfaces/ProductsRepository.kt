package com.example.vendora.domain.repo_interfaces

import com.example.vendora.domain.model.brands.SmartCollection
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    fun getBrands(): Flow<List<SmartCollection>>
}