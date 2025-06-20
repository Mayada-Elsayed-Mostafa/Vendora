package com.example.vendora.domain.usecase.order

import com.example.vendora.domain.model.order.OrderPaymentResult
import com.example.vendora.domain.repo_interfaces.PaymobRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrderPaymentResultUseCase @Inject constructor(
    private val repository: PaymobRepository
) {

    operator fun invoke(id: Int, token: String): Flow<Result<OrderPaymentResult>> {
        return repository.getOrderPaymentResult(id,token)
    }
}