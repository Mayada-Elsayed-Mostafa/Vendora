package com.example.vendora.data.repo_implementation

import com.example.vendora.CartCreateMutation
import com.example.vendora.CartLinesAddMutation
import com.example.vendora.GetCartQuery
import com.example.vendora.data.local.LocalDataSource
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.repo_interfaces.CartRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import com.example.vendora.utils.wrapper.Result
import org.junit.Before
import org.junit.Test


class CartRepositoryImplTest{

    private lateinit var repository: CartRepository
    private val mockRemote = mockk<RemoteDataSource>()
    private val  mockLocalData = mockk<LocalDataSource>(relaxed = true)

    @Before
    fun setup(){
        repository = CartRepositoryImpl(mockRemote,mockLocalData)
    }


    @Test
    fun getCart_emit_Loading_then_Success() = runTest {
        //Given
        val cartId = "Cart655"
        val fakeCart = mockk<GetCartQuery.Cart>()
        val fakeResponse = mockk<GetCartQuery.Data>(){ every { cart } returns fakeCart }

        coEvery { mockRemote.getCart(cartId) } returns fakeResponse

        //when
        val result = repository.getCart(cartId).toList()

        //Then
        assertEquals(Result.Loading, result[0])
        assertEquals(Result.Success(fakeCart), result[1])
    }

    @Test
    fun createCart_emitLoading_thenSuccess() = runTest {
        //Given
        val fakeCart = mockk<CartCreateMutation.Cart> (relaxed = true)
        val fakeCreateCart = mockk<CartCreateMutation.CartCreate>(){
            every { cart } returns fakeCart
        }

        val fakeResponse = mockk<CartCreateMutation.Data>(){
            every { cartCreate } returns fakeCreateCart
        }

        coEvery { mockRemote.createCart() } returns fakeResponse

        //when
        val result = repository.createCart().toList()

        //Then
        assertEquals(Result.Loading, result[0])
        assertEquals(Result.Success(fakeCart), result[1])

    }


    @Test
    fun getCart_should_emit_Failure_when_cart_is_null() = runTest {
        // Given
        val cartId = "cart_123"
        val fakeResponse = mockk<GetCartQuery.Data> {
            every { cart } returns null
        }
        coEvery { mockRemote.getCart(cartId) } returns fakeResponse

        // When
        val result = repository.getCart(cartId).toList()

        // Then
        assertEquals(Result.Loading, result[0])
        assert(result[1] is Result.Failure)

    }

    @Test
    fun addToCart_emitLoading_then_Success() = runTest{
        //Given
        val cartId = "cart655"
        val variantId = "id12313"
        val quantity = 2
        val fakeCart = mockk<CartLinesAddMutation.Cart>()
        val fakeCartLinesAdd = mockk<CartLinesAddMutation.CartLinesAdd>(){
            every { cart } returns fakeCart
        }
        val fakeResponse = mockk<CartLinesAddMutation.Data> {
            every { cartLinesAdd } returns fakeCartLinesAdd
        }

        coEvery { mockRemote.addToCart(any(),any()) } returns fakeResponse

        //when
        val result = repository.addToCart(cartId,variantId,quantity).toList()

        //then
        assertEquals(Result.Loading, result[0])
        assertEquals(Result.Success(fakeCart), result[1])

    }








}