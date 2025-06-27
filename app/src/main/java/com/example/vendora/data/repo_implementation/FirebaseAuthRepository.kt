package com.example.vendora.data.repo_implementation

import com.example.vendora.domain.repo_interfaces.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor() : AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (success: Boolean, isVerified: Boolean, userId: String?, String?, String?, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    onResult(true, user?.isEmailVerified == true, user?.uid, user?.displayName, user?.email, null)
                } else {
                    onResult(false, false, null, null, null, task.exception?.message)
                }
            }
    }

}
