package com.example.vendora.domain.usecase.currency

import com.example.vendora.domain.model.currency.CurrencyInfo
import com.example.vendora.domain.model.currency.CurrencyResponse
import com.example.vendora.domain.repo_interfaces.CurrencyRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(private val repository: CurrencyRepository) {

    operator fun invoke(baseCurrency : String) : Flow<Result<CurrencyResponse>>{
        return repository.getCurrency(baseCurrency)
    }

}