package com.example.vendora.domain.repo_interfaces

interface AuthRepository {
    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (isSuccess: Boolean, isVerified: Boolean, error: String?) -> Unit
    )
}
