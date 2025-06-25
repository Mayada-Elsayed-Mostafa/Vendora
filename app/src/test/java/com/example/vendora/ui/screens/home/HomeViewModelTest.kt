package com.example.vendora.ui.screens.home

import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.usecase.brands.GetBrandsUseCase
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    //dependencies
    private val mockUseCase = mockk<GetBrandsUseCase>()
    private val mockUserPreferences = mockk<UserPreferences>()

    //test environment
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun creatingViewModel_loadingUsernameFromPreferences() = runTest {
        //Given
        coEvery { mockUserPreferences.getUserName() } returns "Zeyad Maamoun"

        // when viewModel is firstly created the init is called
        viewModel = HomeViewModel(
            getBrandsUseCase = mockUseCase,
            userPreferences = mockUserPreferences
        )
        advanceUntilIdle()

        //Then
        assertEquals(viewModel.username.value,"Zeyad Maamoun")
    }

    @Test
    fun fetchBrands_emitLoadingThenSuccess() = runTest {
        //Given
        val fakeResponse = BrandsResponse(smart_collections = emptyList())
        val flow = flowOf(Result.Loading,Result.Success(fakeResponse))
        coEvery { mockUserPreferences.getUserName() } returns null
        coEvery { mockUseCase.invoke() } returns flow
        viewModel = HomeViewModel(
            getBrandsUseCase = mockUseCase,
            userPreferences = mockUserPreferences
        )

        //When
        viewModel.fetchBrands()
        advanceUntilIdle()

        //Then
        val result = viewModel.brands.value
        assertTrue(result is Result.Success)
        assertEquals(fakeResponse,(result as Result.Success).data)
    }
}