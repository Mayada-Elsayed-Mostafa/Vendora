package com.example.vendora.data.remote

import com.example.vendora.domain.model.brands.BrandsResponse

interface RemoteDataSource {
    suspend fun getBrands(): BrandsResponse
}