package com.example.vendora.domain.usecase.addresses

import com.example.vendora.domain.model.address.AddressEntity
import com.example.vendora.domain.repo_interfaces.AddressRepository
import com.example.vendora.utils.wrapper.Result
import javax.inject.Inject

class GetAllAddressesUseCase @Inject constructor(private val addressRepository: AddressRepository) {
    suspend operator fun invoke():Result<List<AddressEntity>>{
        return addressRepository.getAllAddresses()
    }
}

class GetAllAddressesByEmailUseCase @Inject constructor(private val addressRepository: AddressRepository) {
    suspend operator fun invoke(email:String):Result<List<AddressEntity>>{
        return addressRepository.getAllAddressesByEmail(email)
    }
}