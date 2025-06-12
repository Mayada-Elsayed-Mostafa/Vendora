package com.example.vendora.domain.usecase.payment

import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import com.example.vendora.domain.repo_interfaces.PaymobRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentKeyUseCase @Inject constructor(private val repository: PaymobRepository) {
    operator fun invoke (request: PaymentKeyRequest):Flow<Result<PaymentKeyResponse>>{
        return repository.getPaymentKey(request)
    }
}