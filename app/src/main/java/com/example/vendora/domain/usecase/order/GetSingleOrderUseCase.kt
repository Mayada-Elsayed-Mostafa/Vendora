package com.example.vendora.domain.usecase.order

import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.domain.repo_interfaces.OrdersRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSingleOrderUseCase @Inject constructor(
    private val repository: OrdersRepository
){
    operator fun invoke(orderId: Long): Flow<Result<SingleOrderResponse>>{
        return repository.getOrderById(orderId = orderId)
    }
}