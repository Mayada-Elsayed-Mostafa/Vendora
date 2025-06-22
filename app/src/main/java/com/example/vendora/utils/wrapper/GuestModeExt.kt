package com.example.vendora.utils.wrapper

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

fun ViewModel.isGuestMode(): Boolean{
    val auth = FirebaseAuth.getInstance()
    return auth.currentUser == null
}