package com.example.vendora.ui.screens.brandDetails

import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.model.product.Products
import com.example.vendora.domain.usecase.products.GetProductsByBrandIdUseCase
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
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
class BrandDetailsViewModelTest {

    //dependencies
    private var mockUseCase = mockk<GetProductsByBrandIdUseCase>()

    //environment
    private lateinit var viewModel: BrandDetailsViewModel
    private lateinit var testDispatcher: CoroutineDispatcher

    @Before
    fun setup(){
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        viewModel = BrandDetailsViewModel(
            useCase = mockUseCase
        )
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun getProductsByBrandId_collectEmptyListOfProducts() = runTest {
        //Given
        val fakeResponse = Products(products = emptyList())
        val flow = flowOf(Result.Loading,Result.Success(fakeResponse))

        //When
        coEvery { mockUseCase.invoke(any()) } returns flow
        viewModel.getProductsByBrandId(12345L)
        advanceUntilIdle()

        //Then
        val result = viewModel.uiState.value
        assertTrue(result.products is Result.Success)
        assertEquals(fakeResponse.products,(result.products as Result.Success).data)
    }

    @Test
    fun filterProducts_GivingQueryName_returnListOfTwoItems() = runTest(testDispatcher) {
        val query = "name"
        val allProducts = listOf(
            Product(title = "nameOne"),
            Product(title = "nameTwo"),
            Product(title = "three"),
            Product(title = "four"),
        )
        val flow = flowOf(Result.Loading,Result.Success(Products(allProducts)))

        //When
        coEvery { mockUseCase.invoke(any()) } returns flow
        viewModel.getProductsByBrandId(12345L)
        advanceUntilIdle()
        viewModel.filterProducts(query)

        val result = viewModel.uiState.value
        assertTrue(result.filteredProducts.size == 2)
    }

    @Test
    fun resetProducts_filterListEqualsProductsList() = runTest {
        val query = "name"
        val allProducts = listOf(
            Product(title = "nameOne"),
            Product(title = "nameTwo"),
            Product(title = "three"),
            Product(title = "four"),
        )
        val flow = flowOf(Result.Loading,Result.Success(Products(allProducts)))

        //When
        coEvery { mockUseCase.invoke(any()) } returns flow
        viewModel.getProductsByBrandId(12345L)
        advanceUntilIdle()
        viewModel.filterProducts(query)
        viewModel.resetProducts()

        val result = viewModel.uiState.value
        assertTrue(result.filteredProducts.size == 4)
    }
}