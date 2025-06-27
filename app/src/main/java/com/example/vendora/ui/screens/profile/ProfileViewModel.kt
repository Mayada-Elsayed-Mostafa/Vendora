package com.example.vendora.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.data.local.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : ViewModel() {

    private var _userInfo = MutableStateFlow(UserInfo())
    val userInfo = _userInfo.asStateFlow()

//    private var _isGuestMode = MutableStateFlow(true)
//    val isGuestMode = _isGuestMode.asStateFlow()

    fun collectUserState() {
        viewModelScope.launch {
            val userId = userPreferences.getUserId()
            val userName = userPreferences.getUserName()
            val email = userPreferences.getUserEmail()
            val isGuest = userPreferences.isUserLoggedIn()

            if (!userId.isNullOrEmpty() && !email.isNullOrEmpty()) {
                _userInfo.update {
                    it.copy(
                        fullName = userName ?: "User",
                        phoneNumber = "",
                        email = email,
                        isGuest = isGuest
                    )
                }
            } else {
                _userInfo.update {
                    it.copy(
                        fullName = "Guest",
                        phoneNumber = "",
                        email = "",
                        isGuest = isGuest
                    )
                }
            }
        }
    }

    fun logoutUser() {
        auth.signOut()
        viewModelScope.launch {
            userPreferences.saveLoginState(false)
            userPreferences.clearUser()
        }
    }

}

data class UserInfo(
    val fullName: String = "Guest",
    val email: String = "",
    val phoneNumber: String = "",
    val isGuest: Boolean = true
)