package com.example.vendora.ui.cart_screen.viewModel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.model.payment.BillingData
import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import com.example.vendora.domain.usecase.payment.CreateOrderUseCase
import com.example.vendora.domain.usecase.payment.GetPaymentKeyUseCase
import com.example.vendora.domain.usecase.payment.GetTokenForPayMob
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PaymobViewModelTest{

    private lateinit var viewModel: PaymobViewModel

    private val getTokenUseCase = mockk<GetTokenForPayMob>()
    private val createOrderUseCase = mockk<CreateOrderUseCase>()
    private val getPaymentKeyUseCase= mockk<GetPaymentKeyUseCase>()

    //private val testDispatcher = StandardTestDispatcher()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)

        viewModel = PaymobViewModel(getTokenUseCase,createOrderUseCase, getPaymentKeyUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }





    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getTokenAuth_emitSuccessState() = runTest {
        // Given
        val expectedToken = "vddgdgvz"
        val mockResponse = AuthTokenResponse(token = expectedToken)


        coEvery { getTokenUseCase.invoke() } returns flowOf(Result.Success(mockResponse))

        // When
        viewModel.getTokenForAuthentication()
        advanceUntilIdle()

        // Then
        val result = viewModel.getTokenState.value
        assert(result is Result.Success)
        assertEquals(expectedToken, (result as Result.Success).data.token)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun createOrder_emit_Success_result() = runTest {
        //given
        val dummyOrderRequest = OrderRequest(
            auth_token = "fbfgf",
            amount_cents = 10000,
        )

        val dummyOrderResponse = OrderResponse(id = 1010)


        coEvery { createOrderUseCase.invoke(dummyOrderRequest) } returns flowOf(Result.Success(dummyOrderResponse))

        //When

        viewModel.createOrder(dummyOrderRequest)
        advanceUntilIdle()

        //Then
        val result = viewModel.orderState.value
        assert(result is Result.Success)
        assertEquals(1010,(result as Result.Success).data.id)

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getPaymentKey_emit_Success_Result ()= runTest {
        //Given
        val dummyRequest = mockk<PaymentKeyRequest>(relaxed = true)

        val dummyResponse = PaymentKeyResponse("mk10")

        coEvery { getPaymentKeyUseCase.invoke(dummyRequest) } returns flowOf(Result.Success(dummyResponse))

        //when
        viewModel.getPaymentKey(dummyRequest)
        advanceUntilIdle()

        //then
        val result = viewModel.paymentKeyState.value
        assert(result is Result.Success)
        assertEquals("mk10",(result as Result.Success).data.token)


    }



}