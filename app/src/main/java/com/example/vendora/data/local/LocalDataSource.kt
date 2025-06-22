package com.example.vendora.data.local

import com.example.vendora.domain.model.address.AddressEntity

interface LocalDataSource {

    suspend fun getAllAddresses(): List<AddressEntity>

    suspend fun insertAddress(addressEntity: AddressEntity): Long

    suspend fun deleteAddress(addressId: Int): Int

    suspend fun clearDefaultAddress(email: String)

    suspend fun getAddressesByEmail(email: String): List<AddressEntity>

    //Currency

    fun putString(key: String, value: String)

    fun getString(key: String, defaultValue: String): String

    fun saveCurrency(code: String, value: String)

    fun geTCurrency(code: String, defaultValue: String): String

    fun saveSelectedCurrency(code: String)

    fun getSelectedCurrency(): String


}