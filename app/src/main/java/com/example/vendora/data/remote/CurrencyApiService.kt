package com.example.vendora.data.remote

import com.example.vendora.domain.model.currency.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("latest?currencies=EUR%2CUSD%2CEGP")
    suspend fun getCurrency(
        @Query("apikey") apiKey:String,
//        @Query("currencies") currencies:String = "EUR,USD,EGP",
        @Query("base_currency") baseCurrency: String = "EGP"
    ): CurrencyResponse
}