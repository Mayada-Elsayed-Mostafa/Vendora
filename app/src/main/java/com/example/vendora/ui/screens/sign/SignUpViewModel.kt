package com.example.vendora.ui.screens.sign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SignUpState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class SignUpViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState

    fun registerUser(email: String, password: String, confirmPassword: String) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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
                        _signUpState.value =
                            SignUpState(successMessage = "Account created successfully!")
                    } else {
                        val exception = task.exception
                        val errorMessage = when (exception) {
                            is FirebaseAuthUserCollisionException -> "Email already registered"
                            is FirebaseAuthInvalidCredentialsException -> "Invalid email"
                            else -> exception?.message ?: "Unknown error"
                        }
                        _signUpState.value = SignUpState(errorMessage = errorMessage)
                    }
                }
        }
    }


    fun clearMessages() {
        _signUpState.value = _signUpState.value.copy(errorMessage = null, successMessage = null)
    }
}
