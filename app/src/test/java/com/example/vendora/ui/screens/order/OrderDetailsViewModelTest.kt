package com.example.vendora.ui.screens.order

import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.domain.usecase.order.GetSingleOrderUseCase
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrderDetailsViewModelTest {

    //dependencies
    private val mockUseCase = mockk<GetSingleOrderUseCase>()

    //environment
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: OrderDetailsViewModel

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
        viewModel = OrderDetailsViewModel(mockUseCase)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun collectOrder() = runTest{
        //Given
        val fakeResponse = mockk<SingleOrderResponse>()
        val flow = flowOf(Result.Loading,Result.Success(fakeResponse))

        coEvery { mockUseCase.invoke(any()) } returns flow
        viewModel.collectOrder(21213)
        advanceUntilIdle()

        val result = viewModel.order.value
        assertEquals(Result.Success(fakeResponse),result)
    }
}