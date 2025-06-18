package com.example.vendora.domain.repo_interfaces

import com.example.vendora.domain.model.order.OrderWrapper
import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.domain.model.order.UserOrdersResponse
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {
    fun getOrdersByEmail(email: String): Flow<Result<UserOrdersResponse>>

    fun getOrderById(orderId: Long): Flow<Result<SingleOrderResponse>>

    fun createOrder(orderWrapper: OrderWrapper): Flow<Result<SingleOrderResponse>>
}