package com.example.vendora.domain.repo_interfaces

import com.example.vendora.domain.model.address.AddressEntity
import com.example.vendora.domain.model.address.CountryResponse
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    suspend fun getAllAddresses(): Result<List<AddressEntity>>

    suspend fun insertAddress(addressEntity: AddressEntity): Long

    suspend fun deleteAddress(addressId: Int): Int

    suspend fun getAllAddressesByEmail(email:String): Result<List<AddressEntity>>

    ///Country
    fun getCountryById(countryId: Long): Flow<Result<CountryResponse>>

}