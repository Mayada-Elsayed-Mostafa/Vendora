package com.example.vendora.domain.repo_interfaces

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FakeAuthRepository : AuthRepository {
    var shouldSucceed = true
    var isVerified = true
    var error: String? = null

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Boolean, Boolean, String?) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(10)
            onResult(shouldSucceed, isVerified, error)
        }
    }
}
