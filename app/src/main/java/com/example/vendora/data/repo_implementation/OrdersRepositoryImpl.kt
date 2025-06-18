package com.example.vendora.data.repo_implementation

import com.example.vendora.BuildConfig
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.order.OrderWrapper
import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.domain.model.order.UserOrdersResponse
import com.example.vendora.domain.repo_interfaces.OrdersRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val remoteSource: RemoteDataSource
) : OrdersRepository {

    override fun getOrdersByEmail(email: String): Flow<Result<UserOrdersResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteSource.getOrdersByEmail(
                token = BuildConfig.adminApiAccessToken,
                email = email
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    override fun getOrderById(orderId: Long): Flow<Result<SingleOrderResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteSource.getOrderById(
                token = BuildConfig.adminApiAccessToken,
                orderId = orderId
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    override fun createOrder(orderWrapper: OrderWrapper): Flow<Result<SingleOrderResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteSource.createShopifyOrder(
                token = BuildConfig.adminApiAccessToken,
                orderWrapper = orderWrapper
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }


    companion object{
        const val TAG = "OrdersRepositoryImpl"
    }
}