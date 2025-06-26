package com.example.vendora.data.repo_implementation

import com.example.vendora.domain.repo_interfaces.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor() : AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Boolean, Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        onResult(true, true, null)
                    } else {
                        auth.signOut()
                        onResult(false, false, "Please verify your email first.")
                    }
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Unknown error"
                    onResult(false, false, errorMessage)
                }
            }
    }
}
