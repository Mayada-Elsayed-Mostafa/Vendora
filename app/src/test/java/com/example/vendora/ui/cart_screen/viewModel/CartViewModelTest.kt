package com.example.vendora.ui.cart_screen.viewModel

import com.example.vendora.CartCreateMutation
import com.example.vendora.CartLinesAddMutation
import com.example.vendora.CartLinesRemoveMutation
import com.example.vendora.GetCartQuery
import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.repo_interfaces.CartRepository
import com.example.vendora.domain.usecase.cart.AddToCartUseCase
import com.example.vendora.domain.usecase.cart.CreateCartUseCase
import com.example.vendora.domain.usecase.cart.GetCartUseCase
import com.example.vendora.domain.usecase.cart.RemoveAllLinesFromCartUseCase
import com.example.vendora.domain.usecase.cart.RemoveFromCartUseCase
import com.example.vendora.domain.usecase.cart.UpdateCartLineUseCase
import com.example.vendora.type.CurrencyCode
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    private lateinit var viewModel: CartViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var createCartUseCase: CreateCartUseCase
    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var updateCartLineUseCase: UpdateCartLineUseCase
    private lateinit var removeFromCartUseCase: RemoveFromCartUseCase
    private lateinit var getCartUseCase: GetCartUseCase
    private lateinit var removeAllLinesFromCartUseCase: RemoveAllLinesFromCartUseCase
    private lateinit var cartRepository: CartRepository
    private lateinit var userPreferences: UserPreferences

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)

        createCartUseCase = mockk(relaxed = true)
        addToCartUseCase= mockk(relaxed = true)
        updateCartLineUseCase =mockk(relaxed = true)
        removeFromCartUseCase = mockk(relaxed = true)
        getCartUseCase = mockk(relaxed = true)
        removeAllLinesFromCartUseCase = mockk(relaxed = true)
        cartRepository = mockk(relaxed = true)
        userPreferences = mockk(relaxed = true)

        coEvery { userPreferences.getUserEmail() } returns "mk@gmail.com"
        every { cartRepository.getCartId() } returns "cardId10"

        viewModel = CartViewModel(
            createCartUseCase,
            addToCartUseCase,
            updateCartLineUseCase,
            removeFromCartUseCase,
            getCartUseCase,
            cartRepository,
            removeAllLinesFromCartUseCase,
            userPreferences
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun createCart_updateUIState_WithSuccess_Result() = runTest {
        //Given
        val fakeCart = CartCreateMutation.Cart(id="testId", totalQuantity = 2, cost = CartCreateMutation.Cost(totalAmount = CartCreateMutation.TotalAmount(amount = "100.0")))

        coEvery { createCartUseCase.invoke() } returns flowOf(Result.Success(fakeCart))

        //when

        viewModel.createCart()
        advanceUntilIdle()

        //then

        val state = viewModel.uiState.value
        assertEquals(Result.Success(fakeCart),state.createCartResult)
        assertEquals("testId",state.cartId)

    }

    @Test
    fun addToCart_shouldUpdateUiStateWithSuccessResult() = runTest {
        // Given
        val fakeEmail = "test@example.com"
        val fakeCartId = "cart123"
        val variantId = "variant456"
        val quantity = 2
        val fakeCart = CartLinesAddMutation.Cart(
            totalQuantity = 3,
            lines = mockk(relaxed = true),
            cost = mockk< CartLinesAddMutation.Cost>(relaxed = true),
            id = "1010"
        )

        coEvery  {userPreferences.getUserEmail()  } returns fakeEmail
        every { cartRepository.getString(fakeEmail, "") } returns fakeCartId
        coEvery { addToCartUseCase.invoke(fakeCartId, variantId, quantity) } returns flowOf(Result.Success(fakeCart))

        // when
        viewModel.addToCart(variantId, quantity)
        advanceUntilIdle()

        // then
        val state = viewModel.uiState.value
        assertEquals(Result.Success(fakeCart), state.addToCartResult)
        assertEquals(false, state.isAddingToCart)
        assertEquals(3, state.totalQuantity)

    }

    @Test
    fun removeFromCart_updateState_WithSuccessResult()= runTest {
        //given
        val fakeCardId = "cart123"
        val lineId = "123"
        val fakeCart = CartLinesRemoveMutation.Cart(
            totalQuantity = 3,
            id = "1010",
            lines = mockk(relaxed = true),
            cost = mockk(relaxed = true)
        )
        viewModel.setCartIdForTest(fakeCardId)
        coEvery { removeFromCartUseCase.invoke(fakeCardId,lineId) } returns flowOf(Result.Success(fakeCart))
        coEvery { getCartUseCase.invoke(fakeCardId) } returns flowOf(Result.Success(mockk(relaxed = true)))


        //when
        viewModel.removeFromCart(lineId)
        advanceUntilIdle()

        //then
        val state = viewModel.uiState.value
        assertEquals(Result.Success(fakeCart),state.removeFromCartResult)
        assertEquals(3, state.totalQuantity)



    }

    @Test
    fun loadCart_updatesUiStateWithSuccess()= runTest {
        //Given
        val cartId = "Cart655"

        val fakeCart = GetCartQuery.Cart(
            id = cartId,
            totalQuantity = 5,
            cost = GetCartQuery.Cost(
                totalAmount = GetCartQuery.TotalAmount(
                    amount = "250.0",
                    currencyCode = CurrencyCode.EGP
                )
            ),
            lines = mockk(relaxed = true),
            buyerIdentity = mockk(relaxed = true),
            discountCodes = mockk(relaxed = true),
            note = "",
        )

        coEvery { getCartUseCase.invoke(cartId) } returns  flowOf(Result.Success(fakeCart))

        //when
        viewModel.loadCart(cartId)
        advanceUntilIdle()

        //then

        val state = viewModel.uiState.value
        assertEquals(Result.Success(fakeCart),state.loadCartResult)
        assertEquals(5, state.totalQuantity)

    }


}