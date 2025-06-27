package com.example.vendora.domain.repo_interfaces

interface AuthRepository {
    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (
            success: Boolean,
            isVerified: Boolean,
            userId: String?,
            name: String?,
            email: String?,
            error: String?
        ) -> Unit
    )

}
