package com.example.vendora.domain.repo_interfaces

import com.example.vendora.domain.model.currency.CurrencyInfo
import com.example.vendora.domain.model.currency.CurrencyResponse
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getCurrency(baseCurrency: String): Flow<Result<CurrencyResponse>>
}