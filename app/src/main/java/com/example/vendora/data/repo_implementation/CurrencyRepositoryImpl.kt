package com.example.vendora.data.repo_implementation

import com.example.vendora.data.remote.CurrencyApiService
import com.example.vendora.domain.model.currency.CurrencyInfo
import com.example.vendora.domain.model.currency.CurrencyResponse
import com.example.vendora.domain.repo_interfaces.CurrencyRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(private val apiService: CurrencyApiService) : CurrencyRepository  {


    override fun getCurrency(baseCurrency: String): Flow<Result<CurrencyResponse>> = flow {
        emit(Result.Loading)
        val apiKey = "cur_live_mcQbFfeHmOBwgazJYDgaNrW1X3VkvJ6DXPsuu43X"
        try {
            val response = apiService.getCurrency(apiKey,baseCurrency)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Failure(e))
        }

    }
}