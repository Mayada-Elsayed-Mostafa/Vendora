package com.example.vendora.ui.screens.address.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.model.address.AddressEntity
import com.example.vendora.domain.model.address.CountryResponse
import com.example.vendora.domain.model.address.Province
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.usecase.addresses.DeleteAddressUseCase
import com.example.vendora.domain.usecase.addresses.GetAllAddressesByEmailUseCase
import com.example.vendora.domain.usecase.addresses.GetAllAddressesUseCase
import com.example.vendora.domain.usecase.addresses.GetCountryByIdUseCase
import com.example.vendora.domain.usecase.addresses.InsertAddressUseCase
import com.example.vendora.utils.wrapper.Result
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getAllAddressesUseCase: GetAllAddressesUseCase,
    private val insertAddressUseCase: InsertAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    private val getCountryByIdUseCase: GetCountryByIdUseCase,
    private val getAllAddressesByEmailUseCase: GetAllAddressesByEmailUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _address = MutableStateFlow<Result<List<AddressEntity>>>(Result.Loading)
    val address: StateFlow<Result<List<AddressEntity>>> = _address.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _defaultAddress = MutableStateFlow<AddressEntity?>(null)
    val defaultAddress: StateFlow<AddressEntity?> = _defaultAddress

    private val _provinces = MutableStateFlow<Result<CountryResponse>>(Result.Loading)
     val provinces = _provinces.asStateFlow()

    private val _selectedProvince = MutableStateFlow<String?>(null)
    val selectedProvince = _selectedProvince.asStateFlow()

    var email: String? = null


    init {
        email = runBlocking { userPreferences.getUserEmail() }
        getCountry()
    }
    fun getAllAddresses() {
        viewModelScope.launch {
            _address.value = getAllAddressesUseCase()
            updateDefaultAddress()
        }
    }

    fun getAllAddressesByEmail(email: String) {

        viewModelScope.launch {
            _address.value = getAllAddressesByEmailUseCase(email)
            updateDefaultAddress()
        }
    }

    fun insertAddress(addressEntity: AddressEntity) {
        viewModelScope.launch {
            insertAddressUseCase(addressEntity)
            if (email !=null){
                 getAllAddressesByEmail(email!!)
            }

            _message.value = "Address added successfully"
        }
    }


    fun deleteAddress(addressId: Int) {
        viewModelScope.launch {
            deleteAddressUseCase(addressId)
            if (email !=null){
                getAllAddressesByEmail(email!!)
            }
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


    fun getCountry(){
        viewModelScope.launch {
            getCountryByIdUseCase.invoke(countryId = 719296200935)
                .flowOn(Dispatchers.IO)
                .collect{
                    _provinces.value = it
                }
        }
    }

    fun getUserEmail(): String? = runBlocking {
        userPreferences.getUserEmail()
    }

    fun changeSelectedProvince(provinceName: String) {
        _selectedProvince.value = provinceName
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        val regex = Regex("^01[0125]\\d{8}$")
        return regex.matches(phone)
    }


}