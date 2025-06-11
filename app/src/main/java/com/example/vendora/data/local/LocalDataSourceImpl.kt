package com.example.vendora.data.local

import com.example.vendora.domain.model.address.AddressEntity
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val addressDao: AddressDao) : LocalDataSource{
    override suspend fun getAllAddresses(): List<AddressEntity> {
        return addressDao.getAllAddresses()
    }

    override suspend fun insertAddress(addressEntity: AddressEntity): Long {
        return addressDao.insertAddress(addressEntity)
    }

    override suspend fun deleteAddress(addressId: Int): Int {
        return addressDao.deleteAddress(addressId)
    }

    override suspend fun clearDefaultAddress() {
        return addressDao.clearDefaultAddress()
    }
}