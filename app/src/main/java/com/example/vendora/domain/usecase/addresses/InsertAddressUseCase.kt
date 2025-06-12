package com.example.vendora.domain.usecase.addresses

import com.example.vendora.domain.model.address.AddressEntity
import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.repo_interfaces.AddressRepository
import com.example.vendora.domain.repo_interfaces.ProductsRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertAddressUseCase @Inject constructor(private val addressRepository: AddressRepository){
    suspend operator fun invoke(address: AddressEntity):Long{
        return addressRepository.insertAddress(address)
    }
}
