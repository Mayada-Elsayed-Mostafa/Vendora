package com.example.vendora.domain.usecase.customer

import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.customer.CreatedCustomerResponse
import com.example.vendora.domain.model.customer.CustomerRequest
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateCustomerUseCase @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    suspend operator fun invoke(token: String, request: CustomerRequest): Flow<Result<CreatedCustomerResponse>> =
        flow {
            try {
                emit(Result.Loading)
                val response = remoteDataSource.createCustomer(token, request)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Failure(e))
            }
        }
}
