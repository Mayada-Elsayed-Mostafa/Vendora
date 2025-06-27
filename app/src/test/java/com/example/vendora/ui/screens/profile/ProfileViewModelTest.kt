package com.example.vendora.ui.screens.profile

import com.example.vendora.data.local.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    //dependencies
    private val mockUserPreferences = mockk<UserPreferences>()
    private val mockFirebaseAuth = mockk<FirebaseAuth>(relaxed = true)

    //environment
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfileViewModel(mockUserPreferences, mockFirebaseAuth)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun collectUserState() = runTest {
        val fakeUserId = "5626662622"
        val username = "zeyad"
        val fakeEmail = "zeyadmamoun952@gmail.com"

        coEvery { mockUserPreferences.getUserId() } returns fakeUserId
        coEvery { mockUserPreferences.getUserName() } returns username
        coEvery { mockUserPreferences.getUserEmail() } returns fakeEmail

        viewModel.collectUserState()
        advanceUntilIdle()

        val result = viewModel.userInfo.value
        assertEquals(result.email, fakeEmail)
        assertEquals(result.isGuest, false)
    }
}