package com.example.vendora.ui.screens.sign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.model.customer.CreatedCustomerResponse
import com.example.vendora.domain.model.customer.Customer
import com.example.vendora.domain.model.customer.CustomerRequest
import com.example.vendora.domain.repo_interfaces.AuthSignUpRepository
import com.example.vendora.domain.usecase.customer.CreateCustomerUseCase
import com.example.vendora.utils.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val createCustomerUseCase: CreateCustomerUseCase,
    private val userPreferences: UserPreferences,
    private val authRepository: AuthSignUpRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState

    private val _customerState = MutableStateFlow<Result<CreatedCustomerResponse>>(Result.Loading)
    val customerState = _customerState.asStateFlow()

    fun registerUser(
        email: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        shopifyToken: String,
        phone: String
    ) {
        if (!isValidEmail(email)) {
            _signUpState.value = SignUpState(errorMessage = "Invalid email format")
            return
        }

        if (password.length < 6) {
            _signUpState.value =
                SignUpState(errorMessage = "Password must be at least 6 characters")
            return
        }

        if (password != confirmPassword) {
            _signUpState.value = SignUpState(errorMessage = "Passwords do not match")
            return
        }

        _signUpState.value = SignUpState(isLoading = true)

        authRepository.registerUser(email, password) { success, error ->
            if (success) {
                authRepository.sendEmailVerification { verifySuccess, verifyError ->
                    if (verifySuccess) {
                        _signUpState.value = SignUpState(
                            successMessage = "Account created! Please verify your email before logging in."
                        )
                        authRepository.signOut()

                        createShopifyCustomer(
                            token = shopifyToken,
                            email = email,
                            first_name = firstName,
                            last_name = lastName,
                            phone = phone
                        )
                    } else {
                        _signUpState.value = SignUpState(errorMessage = verifyError)
                    }
                }
            } else {
                _signUpState.value = SignUpState(errorMessage = error)
            }
        }
    }

    private fun createShopifyCustomer(
        token: String,
        first_name: String,
        last_name: String,
        email: String,
        phone: String
    ) {
        viewModelScope.launch {
            val customer = Customer(
                first_name = first_name,
                last_name = last_name,
                email = email,
                phone = phone
            )

            val request = CustomerRequest(customer)
            createCustomerUseCase(token, request).collect { result ->
                _customerState.value = result

                if (result is Result.Success) {
                    val createdCustomer = result.data.customer
                    val customerId = createdCustomer.id.toString()
                    val customerName = "${createdCustomer.first_name} ${createdCustomer.last_name}"
                    val customerEmail = createdCustomer.email

                    userPreferences.saveUser(customerId, customerName, customerEmail)
                }
            }
        }
    }

    fun isValidEmail(email: String): Boolean {
        return Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})").matches(email)
    }

    fun clearMessages() {
        _signUpState.value = _signUpState.value.copy(errorMessage = null, successMessage = null)
    }
}
