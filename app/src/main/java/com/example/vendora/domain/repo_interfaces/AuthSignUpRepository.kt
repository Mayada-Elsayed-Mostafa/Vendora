package com.example.vendora.domain.repo_interfaces

interface AuthSignUpRepository {
    fun registerUser(
        email: String,
        password: String,
        onResult: (success: Boolean, error: String?) -> Unit
    )

    fun sendEmailVerification(
        onResult: (success: Boolean, error: String?) -> Unit
    )

    fun signOut()
}
