package com.example.vendora.domain.usecase.payment

import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.repo_interfaces.PaymobRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTokenForPayMob @Inject constructor(private val repository: PaymobRepository) {
    operator fun invoke(): Flow<Result<AuthTokenResponse>>{
        return repository.getToken()
    }
}