package com.example.vendora.domain.usecase.addresses

import com.example.vendora.domain.repo_interfaces.AddressRepository
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(private val addressRepository: AddressRepository) {
    suspend operator fun invoke (addressId:Int): Int{
        return addressRepository.deleteAddress(addressId)
    }
}