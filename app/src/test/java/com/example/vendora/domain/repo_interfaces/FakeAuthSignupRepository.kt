package com.example.vendora.domain.repo_interfaces

class FakeAuthSignupRepository : AuthSignUpRepository {

    var shouldSucceed = true
    var shouldVerifySucceed = true

    override fun registerUser(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        onResult(shouldSucceed, if (shouldSucceed) null else "Registration failed")
    }

    override fun sendEmailVerification(
        onResult: (Boolean, String?) -> Unit
    ) {
        onResult(shouldVerifySucceed, if (shouldVerifySucceed) null else "Verification failed")
    }

    override fun signOut() {
    }
}
