package com.example.vendora.data.repo_implementation

import com.example.vendora.BuildConfig
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.repo_interfaces.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val remoteSource: RemoteDataSource) :
    SearchRepository {

    override suspend fun searchProducts(query: String): List<Product> {
        val response = remoteSource.searchProducts(token = BuildConfig.adminApiAccessToken, query)
        return response.products
    }
}