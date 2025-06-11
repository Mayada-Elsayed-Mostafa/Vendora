package com.example.vendora.data.local

import com.example.vendora.domain.model.address.AddressEntity

interface LocalDataSource {

    suspend fun getAllAddresses(): List<AddressEntity>

    suspend fun insertAddress(addressEntity: AddressEntity): Long

    suspend fun deleteAddress(addressId: Int): Int

    suspend fun clearDefaultAddress()
}