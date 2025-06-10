package com.example.vendora.domain.usecase.payment

import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.repo_interfaces.PaymobRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(private val repository: PaymobRepository) {

    operator fun invoke(request: OrderRequest):Flow<Result<OrderResponse>>{
        return repository.createOrder(request)
    }
}