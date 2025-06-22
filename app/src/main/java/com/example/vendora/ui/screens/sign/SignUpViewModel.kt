package com.example.vendora.ui.screens.sign

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.model.customer.CreatedCustomerResponse
import com.example.vendora.domain.model.customer.Customer
import com.example.vendora.domain.model.customer.CustomerRequest
import com.example.vendora.domain.usecase.customer.CreateCustomerUseCase
import com.example.vendora.utils.wrapper.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
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
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

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
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
                            if (verifyTask.isSuccessful) {
                                _signUpState.value = SignUpState(
                                    successMessage = "Account created! Please verify your email before logging in."
                                )
                                updateFirebaseUserProfile(
                                    "$firstName $lastName",
                                    viewModelScope
                                )
                                auth.signOut()

                                createShopifyCustomer(
                                    token = shopifyToken,
                                    email = email,
                                    first_name = firstName,
                                    last_name = lastName,
                                    phone = phone
                                )
                            } else {
                                _signUpState.value = SignUpState(
                                    errorMessage = "Failed to send verification email: ${verifyTask.exception?.message}"
                                )
                            }
                        }
                    } else {
                        val errorMessage = when (val exception = task.exception) {
                            is FirebaseAuthUserCollisionException -> "Email already registered"
                            is FirebaseAuthInvalidCredentialsException -> "Invalid email"
                            else -> exception?.message ?: "Unknown error"
                        }
                        _signUpState.value = SignUpState(errorMessage = errorMessage)
                    }
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
            Log.d("TOKEN_CHECK", "Token: $token")

            val request = CustomerRequest(customer)
            createCustomerUseCase(token, request)
                .collect { result ->
                    _customerState.value = result
                    Log.d("TAG", "createShopifyCustomer: ${result}")

                    if (result is Result.Success) {
                        val createdCustomer = result.data.customer
                        val customerId = createdCustomer.id.toString()
                        val customerEmail = createdCustomer.email

                        userPreferences.saveUser(customerId, customerEmail)
                        Log.d("TAG", "User saved in DataStore: $customerId, $customerEmail")
                    }
                }
        }
    }

    fun clearMessages() {
        _signUpState.value = _signUpState.value.copy(errorMessage = null, successMessage = null)
    }
}
