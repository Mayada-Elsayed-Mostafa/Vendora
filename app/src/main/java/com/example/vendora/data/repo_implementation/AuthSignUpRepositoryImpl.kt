package com.example.vendora.data.repo_implementation

import com.example.vendora.domain.repo_interfaces.AuthSignUpRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthSignUpRepositoryImpl @Inject constructor() : AuthSignUpRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun registerUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    override fun sendEmailVerification(onResult: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        if (user != null) {
            user.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult(true, null)
                    } else {
                        onResult(false, task.exception?.message)
                    }
                }
        } else {
            onResult(false, "User not logged in")
        }
    }

    override fun signOut() {
        auth.signOut()
    }
}
