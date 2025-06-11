package com.example.vendora.ui.screens.address.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.domain.model.address.AddressEntity
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.usecase.addresses.DeleteAddressUseCase
import com.example.vendora.domain.usecase.addresses.GetAllAddressesUseCase
import com.example.vendora.domain.usecase.addresses.InsertAddressUseCase
import com.example.vendora.utils.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getAllAddressesUseCase: GetAllAddressesUseCase,
    private val insertAddressUseCase: InsertAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) : ViewModel() {

    private val _address = MutableStateFlow<Result<List<AddressEntity>>>(Result.Loading)
    val address: StateFlow<Result<List<AddressEntity>>> = _address.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _defaultAddress = MutableStateFlow<AddressEntity?>(null)
    val defaultAddress: StateFlow<AddressEntity?> = _defaultAddress

    fun getAllAddresses() {
        viewModelScope.launch {
            _address.value = getAllAddressesUseCase()
            updateDefaultAddress()
        }
    }

    fun insertAddress(addressEntity: AddressEntity) {
        viewModelScope.launch {
            insertAddressUseCase(addressEntity)
            getAllAddresses()
            _message.value = "Address added successfully"
        }
    }


    fun deleteAddress(addressId: Int) {
        viewModelScope.launch {
            deleteAddressUseCase(addressId)
            getAllAddresses()
            _message.value = "Address deleted"
        }
    }

    private fun updateDefaultAddress() {
        val currentAddresses = (_address.value as? Result.Success)?.data
        _defaultAddress.value = currentAddresses?.firstOrNull { it.isDefault }
    }

    fun clearMessage() {
        _message.value = null
    }

}