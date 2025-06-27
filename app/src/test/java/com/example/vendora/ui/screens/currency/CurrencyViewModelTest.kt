package com.example.vendora.ui.screens.currency

import com.example.vendora.domain.model.currency.CurrencyInfo
import com.example.vendora.domain.model.currency.CurrencyResponse
import com.example.vendora.domain.usecase.currency.GetCurrencyRatesUseCase
import com.example.vendora.domain.usecase.currency.GetCurrencyUseCase
import com.example.vendora.domain.usecase.currency.GetRatesUseCase
import com.example.vendora.domain.usecase.currency.GetSelectedCurrencyUseCase
import com.example.vendora.domain.usecase.currency.SaveCurrencyUseCase
import com.example.vendora.domain.usecase.currency.SaveSelectedCurrencyUseCase
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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
class CurrencyViewModelTest{

    private lateinit var viewModel: CurrencyViewModel
    private val testDispatcher = UnconfinedTestDispatcher()


    private lateinit var getRatesUseCase: GetRatesUseCase
    private lateinit var getCurrencyRatesUseCase: GetCurrencyRatesUseCase
    private lateinit var saveCurrencyUseCase: SaveCurrencyUseCase
    private lateinit var getCurrencyUseCase: GetCurrencyUseCase
    private lateinit var saveSelectedCurrencyUseCase: SaveSelectedCurrencyUseCase
    private lateinit var getSelectedCurrencyUseCase: GetSelectedCurrencyUseCase

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)

        getRatesUseCase = mockk(relaxed = true)
        getCurrencyRatesUseCase = mockk(relaxed = true)
        saveCurrencyUseCase =mockk(relaxed = true)
        getCurrencyUseCase =mockk(relaxed = true)
        saveSelectedCurrencyUseCase = mockk(relaxed = true)
        getSelectedCurrencyUseCase =mockk(relaxed = true)


        viewModel = CurrencyViewModel(
            getCurrencyRatesUseCase,
            getRatesUseCase,
            saveCurrencyUseCase,
            getCurrencyUseCase,
            saveSelectedCurrencyUseCase,
            getSelectedCurrencyUseCase
        )
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }


    @Test
    fun changeSelectedCurrency_updateState_AndSaveCurrency()= runTest {
        //Given
        val newCurrency = "USD"
        every { getCurrencyUseCase.invoke("USD","EGP") } returns 49.0

        //when

        viewModel.changeSelectedCurrency(newCurrency)


        //then
        assertEquals(newCurrency,viewModel.selectedCurrency.value)
        assertEquals(49.0,viewModel.getChangeRate.value)

    }





    @Test
    fun getCurrency_update_currencies_with_success_result() = runTest {

        //given
        val expectedResponse = CurrencyResponse(
           data =  mapOf("USD" to CurrencyInfo("USD",49.0))
        )
        coEvery { getCurrencyRatesUseCase.invoke("EGP") } returns flowOf(Result.Success(expectedResponse))

        //when
        viewModel.getCurrency("EGP")
        advanceUntilIdle()
        //
        assertTrue(viewModel.currencies.value is Result.Success)
        val result = viewModel.currencies.value as Result.Success
        assertEquals(expectedResponse,result.data)

    }


    @Test
    fun getRates_updateRates_SaveCurrencyForAllItems () = runTest {
        //given
        val mockRates = listOf(CurrencyInfo("USD",0.032),CurrencyInfo("EUR", 0.030))

        coEvery { getRatesUseCase.invoke() } returns flowOf(Result.Success(mockRates))



        ///when
        viewModel.getRates()
        advanceUntilIdle()

        //then
        assertTrue(viewModel.rates.value is Result.Success)
        val result = viewModel.rates.value as Result.Success
        assertEquals(mockRates,result.data)



    }


    @Test
    fun getSelectedCurrency_updateState_returnCorrectValue() = runTest {
        // Given
        val expectedCurrency = "USD"
        every { getSelectedCurrencyUseCase.invoke() } returns expectedCurrency


        // When
        val result = viewModel.getSelectedCurrency()

        // Then
        assertEquals(expectedCurrency, result)
        assertEquals(expectedCurrency, viewModel.selectedCurrency.value)
    }


}