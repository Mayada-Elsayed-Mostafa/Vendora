package com.example.vendora.ui.screens.order

import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.model.order.Item
import com.example.vendora.domain.model.order.OrderPaymentResult
import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.domain.usecase.order.CreateShopifyOrderUserCase
import com.example.vendora.domain.usecase.order.GetOrderPaymentResultUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class PaymentResultViewModelTest {

    private lateinit var viewModel: PaymentResultViewModel
    private lateinit var createShopifyOrderUserCase: CreateShopifyOrderUserCase
    private lateinit var paymentResultUseCase: GetOrderPaymentResultUseCase
    private lateinit var userPreferences: UserPreferences

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        createShopifyOrderUserCase = mockk()
        paymentResultUseCase = mockk()
        userPreferences = mockk()

        viewModel = PaymentResultViewModel(
            createShopifyOrderUserCase,
            paymentResultUseCase,
            userPreferences
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getOrderResult updates uiState with result`() = runTest {
        val fakeResponse = mockk<OrderPaymentResult>(relaxed = true)
        val fakeResult = Result.Success(fakeResponse)
        val orderId = 123
        val token = "abc"

        coEvery {
            paymentResultUseCase.invoke(orderId, "Bearer $token")
        } returns flowOf(fakeResult)

        viewModel.getOrderResult(orderId, token)
        advanceUntilIdle()

        assertEquals(fakeResult, viewModel.uiState.value.result)
    }

//    @Test
//    fun `createOrder updates uiState with creation result`() = runTest {
//        val orderPaymentResult = mockk<OrderPaymentResult>(relaxed = true)
//        val singleOrderResponse = mockk<SingleOrderResponse>(relaxed = true)
//        val expectedResult = Result.Success(singleOrderResponse)
//
//        coEvery { userPreferences.getUserEmail() } returns "test@example.com"
//        coEvery { createShopifyOrderUserCase.invoke(any()) } returns flowOf(expectedResult)
//
//        viewModel.createOrder(orderPaymentResult)
//        advanceUntilIdle()
//
//        assertEquals(expectedResult, viewModel.uiState.value.orderCreationResult)
//    }
}
