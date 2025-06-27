package com.example.vendora.utils.wrapper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.data.local.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//fun ViewModel.isGuestMode(): Boolean{
//    val auth = FirebaseAuth.getInstance()
//    return auth.currentUser == null
//}