package com.example.vendora.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private var _userInfo = MutableStateFlow(UserInfo())
    val userInfo = _userInfo.asStateFlow()

    fun collectUserState(){
        val user = auth.currentUser
        if (user != null){
            _userInfo.update {
                it.copy(
                    fullName = user.displayName ?: "Guest",
                    phoneNumber = "",
                    email = user.email ?: "",
                    isGuest = false
                )
            }
        } else {
            _userInfo.update {
                it.copy(
                    fullName = "Guest",
                    phoneNumber = "",
                    email = "",
                    isGuest = true
                )
            }
        }
//        Log.d("Profile",_userInfo.value.isGuest.toString())
    }

    fun logoutUser(){
        auth.signOut()
    }
}

data class UserInfo(
    val fullName: String = "Guest",
    val email: String = "",
    val phoneNumber: String = "",
    val isGuest: Boolean = true
)