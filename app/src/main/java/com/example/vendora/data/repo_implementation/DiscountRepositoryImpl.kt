package com.example.vendora.data.repo_implementation

import com.example.vendora.BuildConfig
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.discount.DiscountCode
import com.example.vendora.domain.repo_interfaces.DiscountRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DiscountRepositoryImpl @Inject constructor(private val remoteSource: RemoteDataSource) : DiscountRepository {
    override fun fetchDiscountCodes(): Flow<Result<List<DiscountCode>>> = flow {
        emit(Result.Loading)
        try {
            val result = remoteSource.getDiscountCodes(BuildConfig.adminApiAccessToken)
            emit(Result.Success(result))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

}