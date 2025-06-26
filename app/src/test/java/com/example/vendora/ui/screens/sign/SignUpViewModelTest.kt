package com.example.vendora.ui.screens.sign

import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.repo_interfaces.FakeAuthSignupRepository
import com.example.vendora.domain.usecase.customer.CreateCustomerUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SignUpViewModelTest {

    private lateinit var viewModel: SignUpViewModel
    private lateinit var fakeAuthRepository: FakeAuthSignupRepository
    private val fakeUseCase: CreateCustomerUseCase = mockk(relaxed = true)
    private val mockPrefs: UserPreferences = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthSignupRepository()
        viewModel = SignUpViewModel(fakeUseCase, mockPrefs, fakeAuthRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `registerUser with valid input shows success message`() = runTest {
        fakeAuthRepository.shouldSucceed = true
        fakeAuthRepository.shouldVerifySucceed = true

        viewModel.registerUser(
            email = "mayada@example.com",
            password = "123456",
            confirmPassword = "123456",
            firstName = "Mayada",
            lastName = "Elsayed",
            shopifyToken = "token",
            phone = "01012345678"
        )

        advanceUntilIdle()
        Assert.assertEquals(
            "Account created! Please verify your email before logging in.",
            viewModel.signUpState.value.successMessage
        )
    }

    @Test
    fun `registerUser with mismatched passwords returns error`() = runTest {
        viewModel.registerUser(
            email = "mayada@example.com",
            password = "123456",
            confirmPassword = "123455",
            firstName = "Mayada",
            lastName = "Elsayed",
            shopifyToken = "token",
            phone = "01012345678"
        )

        Assert.assertEquals("Passwords do not match", viewModel.signUpState.value.errorMessage)
    }
}
