package com.example.vendora.ui.screens.sign

import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.repo_interfaces.FakeAuthRepository
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SignInViewModelTest {

    private lateinit var viewModel: SignInViewModel
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private val mockPrefs: UserPreferences = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthRepository()
        viewModel = SignInViewModel(mockPrefs, fakeAuthRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `signInUser with invalid email returns error`() = runTest {
        viewModel.signInUser("invalidEmail", "123456")
        assertEquals("Invalid email format", viewModel.signInState.value.errorMessage)
    }

    @Test
    fun `signInUser with successful sign in updates success state`() = runTest {
        fakeAuthRepository.shouldSucceed = true
        fakeAuthRepository.isVerified = true
        viewModel.signInUser("mayada@gmail.com", "123456")
        advanceUntilIdle()
        assertEquals(true, viewModel.signInState.value.isSuccess)
    }
}
