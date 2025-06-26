package com.example.vendora.ui.screens.sign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.repo_interfaces.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signInState = MutableStateFlow(SignInState())
    val signInState: StateFlow<SignInState> = _signInState

    fun signInUser(email: String, password: String) {
        if (!isValidEmail(email)) {
            _signInState.value = SignInState(errorMessage = "Invalid email format")
            return
        }

        if (password.isEmpty()) {
            _signInState.value = SignInState(errorMessage = "Password cannot be empty")
            return
        }

        _signInState.value = SignInState(isLoading = true)

        authRepository.signInWithEmailAndPassword(email, password) { success, isVerified, error ->
            viewModelScope.launch {
                if (success && isVerified) {
                    userPreferences.saveLoginState(true)
                    _signInState.value = SignInState(
                        isSuccess = true,
                        successMessage = "Signed in successfully!"
                    )
                } else {
                    _signInState.value = SignInState(errorMessage = error)
                }
            }
        }
    }

    fun isValidEmail(email: String): Boolean {
        return Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})").matches(email)
    }

    fun clearMessages() {
        _signInState.value = _signInState.value.copy(errorMessage = null, successMessage = null)
    }
}
