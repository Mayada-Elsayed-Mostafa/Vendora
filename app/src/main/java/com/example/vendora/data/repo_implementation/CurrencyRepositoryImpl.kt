package com.example.vendora.data.repo_implementation

import com.example.vendora.data.local.LocalDataSource
import com.example.vendora.data.remote.CurrencyApiService
import com.example.vendora.domain.model.currency.CurrencyInfo
import com.example.vendora.domain.model.currency.CurrencyResponse
import com.example.vendora.domain.repo_interfaces.CurrencyRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(private val apiService: CurrencyApiService , private val localDataSource: LocalDataSource) : CurrencyRepository  {


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

    override fun getRates(): Flow<Result<List<CurrencyInfo>>> = flow {
        emit(Result.Loading)
        val apiKey = "cur_live_mcQbFfeHmOBwgazJYDgaNrW1X3VkvJ6DXPsuu43X"
        try {
            val response = apiService.getCurrency(apiKey)
            emit(Result.Success(response.data.values.toList()))
        } catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    override fun saveCurrency(code: String, value: String) {
        localDataSource.saveCurrency(code,value)
    }

    override fun geTCurrency(code: String, defaultValue: String): String {
        return localDataSource.geTCurrency(code,defaultValue)
    }

    override fun saveSelectedCurrency(code: String) {
        localDataSource.saveSelectedCurrency(code)
    }

    override fun getSelectedCurrency(): String {
        return localDataSource.getSelectedCurrency()
    }
}