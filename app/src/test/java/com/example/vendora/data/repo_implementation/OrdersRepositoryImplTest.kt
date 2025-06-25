package com.example.vendora.data.repo_implementation

import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.order.OrderWrapper
import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.domain.model.order.UserOrdersResponse
import com.example.vendora.domain.repo_interfaces.OrdersRepository
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test


class OrdersRepositoryImplTest {

    private val mockRemote: RemoteDataSource = mockk<RemoteDataSource>()
    private lateinit var repository: OrdersRepository

    @Before
    fun setup() {
        repository = OrdersRepositoryImpl(mockRemote)
    }

    @Test
    fun getOrdersByEmail_twoEmissions_LoadingThenSuccess() = runTest{
        //When
        val fakeResponse = mockk<UserOrdersResponse>(relaxed = true)
        val fakeEmail = "zeyad952@gmail.com"

        //When calling getOrdersByEmail()
        coEvery { mockRemote.getOrdersByEmail(any(),fakeEmail) } returns fakeResponse
        val result = repository.getOrdersByEmail(fakeEmail).toList()

        //Then
        assertThat(result.size, `is`(2))
        assertThat(result[0], `is`(Result.Loading))
        assertThat(result[1], `is`(Result.Success(fakeResponse)))
    }

    @Test
    fun getOrderById_twoEmissions_LoadingThenSuccess() = runTest{
        //When
        val fakeResponse = mockk<SingleOrderResponse>(relaxed = true)
        val fakeOrderId: Long = 123456

        //When calling getOrderById()
        coEvery { mockRemote.getOrderById(any(),fakeOrderId) } returns fakeResponse
        val result = repository.getOrderById(fakeOrderId).toList()

        //Then
        assertThat(result.size, `is`(2))
        assertThat(result[0], `is`(Result.Loading))
        assertThat(result[1], `is`(Result.Success(fakeResponse)))
    }


    @Test
    fun createOrder_twoEmissions_LoadingThenSuccess() = runTest{
        //When
        val fakeBody = mockk<OrderWrapper>(relaxed = true)
        val fakeResponse = mockk<SingleOrderResponse>(relaxed = true)

        //When calling getOrderById()
        coEvery { mockRemote.createShopifyOrder(any(),fakeBody) } returns fakeResponse
        val result = repository.createOrder(fakeBody).toList()

        //Then
        assertThat(result.size, `is`(2))
        assertThat(result[0], `is`(Result.Loading))
        assertThat(result[1], `is`(Result.Success(fakeResponse)))
    }
}