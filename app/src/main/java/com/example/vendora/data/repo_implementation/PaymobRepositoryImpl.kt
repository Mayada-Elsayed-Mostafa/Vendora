package com.example.vendora.data.repo_implementation

import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import com.example.vendora.domain.repo_interfaces.PaymobRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PaymobRepositoryImpl @Inject constructor(
    private val remoteSource: RemoteDataSource
) : PaymobRepository{

    override fun getToken(): Flow<Result<AuthTokenResponse>> = flow {
        emit(Result.Loading)
        try {
            val apiKey = "ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmpiR0Z6Y3lJNklrMWxjbU5vWVc1MElpd2ljSEp2Wm1sc1pWOXdheUk2TVRBMU1EZzNOaXdpYm1GdFpTSTZJbWx1YVhScFlXd2lmUS5FMzk4Mkw3ZWZPeERncjV3T3pVbExUSkdnenU5NHlXSnNHcENrcWhPdFEteW14LU40aUVBM3hVZ3pMdmxZSUJxdGp0ZlBlaHRkTElqSnpDNmVQa0RwUQ=="
            val response = remoteSource.getAuthToken(apiKey)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    override fun createOrder(orderRequest: OrderRequest): Flow<Result<OrderResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteSource.createOrder(orderRequest)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    override fun getPaymentKey(paymentKeyRequest: PaymentKeyRequest): Flow<Result<PaymentKeyResponse>> = flow{
        emit(Result.Loading)
        try {
            val response = remoteSource.getPaymentKey(paymentKeyRequest)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }
}