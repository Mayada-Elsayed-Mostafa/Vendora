package com.example.vendora.data.repo_implementation

import com.example.vendora.BuildConfig
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.repo_interfaces.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.vendora.utils.wrapper.Result
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val remoteSource: RemoteDataSource): ProductsRepository {

    override fun getBrands(): Flow<Result<BrandsResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteSource.getBrands(BuildConfig.adminApiAccessToken)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

}