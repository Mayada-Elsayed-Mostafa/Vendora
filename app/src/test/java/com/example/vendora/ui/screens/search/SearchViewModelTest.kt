package com.example.vendora.ui.screens.search

import com.example.vendora.domain.usecase.search.SearchProductsUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchProductsUseCase: SearchProductsUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchProductsUseCase = mockk()
        viewModel = SearchViewModel(searchProductsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateSearchQuery should update searchQuery state`() = runTest {
        val query = "Shirt"
        viewModel.updateSearchQuery(query)
        assertEquals(query, viewModel.searchQuery.value)
    }

}
