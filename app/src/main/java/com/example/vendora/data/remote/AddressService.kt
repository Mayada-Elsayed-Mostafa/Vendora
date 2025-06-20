package com.example.vendora.data.remote

import com.example.vendora.domain.model.address.CountryResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AddressService {

    @GET("admin/api/2025-04/countries/{country_id}.json")
    suspend fun getCountryById(
        @Header("X-Shopify-Access-Token") token: String,
        @Path("country_id") countryId: Long = 719296200935
    ): CountryResponse

}