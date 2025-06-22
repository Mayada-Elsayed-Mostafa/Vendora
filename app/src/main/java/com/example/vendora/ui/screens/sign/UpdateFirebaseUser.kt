package com.example.vendora.ui.screens.sign

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun SignUpViewModel.updateFirebaseUserProfile(
    fullName: String,
    viewModelScope: CoroutineScope
) {
    val user = FirebaseAuth.getInstance().currentUser ?: return

    viewModelScope.launch {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(fullName)
            .build()

        user.updateProfile(profileUpdates)
    }
}