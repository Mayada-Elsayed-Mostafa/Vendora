package com.example.vendora.ui.screens.discount.viewModel

import com.example.vendora.domain.model.discount.DiscountCode
import com.example.vendora.domain.usecase.discount.GetDiscountCodesUseCase
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
class DiscountViewModelTest{

    private lateinit var discountCodesUseCase: GetDiscountCodesUseCase
    private lateinit var viewModel: DiscountViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        discountCodesUseCase = mockk<GetDiscountCodesUseCase>()

        val fakeList = listOf(DiscountCode(id = 120, price_rule_id = 10, code = "MAD45", usage_count = 1, created_at = ""))
        coEvery { discountCodesUseCase.invoke() } returns flowOf(Result.Success(fakeList))

        viewModel = DiscountViewModel(discountCodesUseCase)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }


    @Test
    fun getDiscountCode_emit_Success_Result()= runTest {
        //given
        val fakeList = listOf(DiscountCode(id = 120, price_rule_id = 10,"MAD45",1,""))
        coEvery { discountCodesUseCase.invoke() } returns flowOf(Result.Success(fakeList))

        //when
        viewModel.getDiscountCode()
        advanceUntilIdle()
        //then
        val result = viewModel.discountCodes.value
        assert(result is Result.Success)
        assertEquals("MAD45", (result as Result.Success).data.first().code)

    }

    @Test
    fun isDiscountCodeValid_should_return_true_for_existing_code()= runTest {
        //given
        val code = "MAD45"


        //when
        advanceUntilIdle()
        val result = viewModel.isDiscountCodeValid(code)

        //Then
        assert(result)

    }


    @Test
    fun calcFinalPriceWithCode_calCorrectDiscount_for_validCode () = runTest {
        //Given
        val totalPrice = 100.0
        val afterDiscount = 90.0
        val code = "MAD45"
        val fakeList = listOf(DiscountCode(id = 1, price_rule_id = 1, code = code, usage_count = 1, created_at = ""))

        coEvery { discountCodesUseCase.invoke() } returns flowOf(Result.Success(fakeList))

        //when
        viewModel.calculateFinalPriceWithCode(code,totalPrice)


        //then

        assertEquals(afterDiscount,viewModel.finalPrice.value)


    }



}