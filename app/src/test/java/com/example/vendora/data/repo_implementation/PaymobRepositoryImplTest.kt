package com.example.vendora.data.repo_implementation

import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.model.payment.BillingData
import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import com.example.vendora.domain.repo_interfaces.PaymobRepository
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class PaymobRepositoryImplTest{
    private lateinit var repository: PaymobRepository
    private val mockRemote = mockk<RemoteDataSource>()

    @Before
    fun setup(){
        repository = PaymobRepositoryImpl(mockRemote)
    }

    @Test
    fun getToken_emitLoading_thenSuccess ()= runTest{
        //Given
        val  fakeResponse = AuthTokenResponse(token = "vndkjnknkj")
        coEvery { mockRemote.getAuthToken(any()) } returns fakeResponse

        //When
        val result = repository.getToken().toList()

        //Then
        assertEquals(Result.Loading,result[0])
        assertEquals(Result.Success(fakeResponse),result[1])
    }

    @Test
    fun createOrder_emitLoading_thenSuccess()= runTest{
        //Given
        val orderRequest = OrderRequest(
            auth_token = "fbfgf",
            amount_cents = 10000,
        )
        val fakeResponse = OrderResponse(id = 1010)
        coEvery { mockRemote.createOrder(orderRequest) } returns fakeResponse

        //when
        val result = repository.createOrder(orderRequest).toList()

        //then
        assertEquals(Result.Loading,result[0])
        assertEquals(Result.Success(fakeResponse),result[1])
    }

    @Test
    fun `getPaymentKey should emit Loading then Success`() = runTest {
        // Given
        val request = PaymentKeyRequest(
            auth_token = "cdc",
            amount_cents = 10000,
            order_id = 1234,
            integration_id = 1000,
            billing_data = BillingData()
        )

        val fakeResponse = PaymentKeyResponse(token = "hhhhhhhhhhscscs")

        coEvery { mockRemote.getPaymentKey(request) } returns fakeResponse

        // When
        val resultList = repository.getPaymentKey(request).toList()

        // Then
        assertEquals(Result.Loading, resultList[0])
        assertEquals(Result.Success(fakeResponse), resultList[1])
    }


}