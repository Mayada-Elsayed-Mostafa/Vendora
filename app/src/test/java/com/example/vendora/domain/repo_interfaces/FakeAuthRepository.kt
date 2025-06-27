package com.example.vendora.domain.repo_interfaces

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FakeAuthRepository : AuthRepository {
    var shouldSucceed = true
    var isVerified = true
    var fakeUserId: String? = "test_user_id"
    var fakeName: String? = "Test User"
    var fakeEmail: String? = "test@example.com"
    var error: String? = null

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (
            success: Boolean,
            isVerified: Boolean,
            userId: String?,
            name: String?,
            emailAddress: String?,
            error: String?
        ) -> Unit
    ) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            if (shouldSucceed) {
                onResult(true, isVerified, fakeUserId, fakeName, fakeEmail, null)
            } else {
                onResult(false, false, null, null, null, error ?: "Unknown error")
            }
        }
    }
}
