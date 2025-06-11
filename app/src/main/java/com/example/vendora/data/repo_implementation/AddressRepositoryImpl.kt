package com.example.vendora.data.repo_implementation

import com.example.vendora.data.local.AddressDao
import com.example.vendora.domain.model.address.AddressEntity
import com.example.vendora.domain.repo_interfaces.AddressRepository
import com.example.vendora.utils.wrapper.Result
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(private val addressDao: AddressDao) : AddressRepository {
    override suspend fun getAllAddresses(): Result<List<AddressEntity> >{
        return Result.Success(addressDao.getAllAddresses())
    }

    override suspend fun insertAddress(addressEntity: AddressEntity): Long {
        if (addressEntity.isDefault){
            addressDao.clearDefaultAddress()
        }
        return addressDao.insertAddress(addressEntity)
    }

    override suspend fun deleteAddress(addressId: Int): Int {
        return addressDao.deleteAddress(addressId)
    }


}