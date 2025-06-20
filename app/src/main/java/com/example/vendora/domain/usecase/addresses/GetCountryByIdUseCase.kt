package com.example.vendora.domain.usecase.addresses

import com.example.vendora.domain.model.address.CountryResponse
import com.example.vendora.domain.repo_interfaces.AddressRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCountryByIdUseCase @Inject constructor(val repository: AddressRepository) {

    operator fun  invoke (countryId: Long): Flow<Result<CountryResponse>>{
        return repository.getCountryById(countryId)
    }
}