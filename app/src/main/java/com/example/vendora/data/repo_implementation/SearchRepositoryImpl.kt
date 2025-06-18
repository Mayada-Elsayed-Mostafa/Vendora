package com.example.vendora.data.repo_implementation

import com.example.vendora.BuildConfig
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.repo_interfaces.SearchRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val remoteSource: RemoteDataSource) :
    SearchRepository {

    private val token = BuildConfig.adminApiAccessToken
    override fun searchProducts(query: String): Flow<Result<List<Product>>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteSource.searchProducts(
                token = token,
                query = query
            )
            emit(Result.Success(response.products))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }
}