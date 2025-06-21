package com.example.vendora.data.repo_implementation

import com.example.vendora.BuildConfig
import com.example.vendora.data.local.AddressDao
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.address.AddressEntity
import com.example.vendora.domain.model.address.CountryResponse
import com.example.vendora.domain.repo_interfaces.AddressRepository
import com.example.vendora.utils.wrapper.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(private val addressDao: AddressDao , private val remoteDataSource: RemoteDataSource) : AddressRepository {
    override suspend fun getAllAddresses(): Result<List<AddressEntity> >{
        return Result.Success(addressDao.getAllAddresses())
    }

    override suspend fun getAllAddressesByEmail(email:String): Result<List<AddressEntity>> {
        return Result.Success(addressDao.getAddressesByEmail(email))
    }

    override suspend fun insertAddress(addressEntity: AddressEntity): Long {
        if (addressEntity.isDefault){
            addressDao.clearDefaultAddress(addressEntity.email)
        }
        return addressDao.insertAddress(addressEntity)
    }

    override suspend fun deleteAddress(addressId: Int): Int {
        return addressDao.deleteAddress(addressId)
    }

    override fun getCountryById(countryId: Long): Flow<Result<CountryResponse>> = flow{
        emit(Result.Loading)
        try {
            val response = remoteDataSource.getCountryById(token = BuildConfig.adminApiAccessToken, countryId = countryId)
            emit(Result.Success(response))
        }catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }


}