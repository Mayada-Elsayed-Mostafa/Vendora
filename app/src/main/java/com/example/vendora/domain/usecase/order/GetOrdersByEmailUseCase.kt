package com.example.vendora.domain.usecase.order

import com.example.vendora.domain.model.order.UserOrdersResponse
import com.example.vendora.domain.repo_interfaces.OrdersRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrdersByEmailUseCase @Inject constructor(
    private val repository: OrdersRepository
) {
    operator fun invoke(email: String): Flow<Result<UserOrdersResponse>>{
        return repository.getOrdersByEmail(
            email = email
        )
    }
}