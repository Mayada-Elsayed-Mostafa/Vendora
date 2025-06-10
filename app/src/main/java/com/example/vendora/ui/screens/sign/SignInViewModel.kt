package com.example.vendora.ui.screens.sign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SignInState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class SignInViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _signInState = MutableStateFlow(SignInState())
    val signInState: StateFlow<SignInState> = _signInState

    fun signInUser(email: String, password: String) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _signInState.value = SignInState(errorMessage = "Invalid email format")
            return
        }

        if (password.isEmpty()) {
            _signInState.value = SignInState(errorMessage = "Password cannot be empty")
            return
        }

        _signInState.value = SignInState(isLoading = true)

        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {
                            _signInState.value =
                                SignInState(successMessage = "Signed in successfully!")
                        } else {
                            auth.signOut()
                            _signInState.value =
                                SignInState(errorMessage = "Please verify your email first.")
                        }
                    } else {
                        val errorMessage = when (val exception = task.exception) {
                            is FirebaseAuthInvalidUserException -> "No account found with this email"
                            is FirebaseAuthInvalidCredentialsException -> "Incorrect email or password"
                            else -> exception?.message ?: "Unknown error"
                        }
                        _signInState.value = SignInState(errorMessage = errorMessage)
                    }
                }
        }
    }

    fun clearMessages() {
        _signInState.value = _signInState.value.copy(errorMessage = null, successMessage = null)
    }
}
