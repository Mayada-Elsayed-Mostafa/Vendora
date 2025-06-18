package com.example.vendora.data.repo_implementation

import com.example.vendora.BuildConfig
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.repo_interfaces.ProductRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteSource: RemoteDataSource
) : ProductRepository {

    private val token = BuildConfig.adminApiAccessToken

    override fun getProductById(productId: Long): Flow<Result<Product>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteSource.getProductById(token, productId)
            emit(Result.Success(response.product))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }
}
