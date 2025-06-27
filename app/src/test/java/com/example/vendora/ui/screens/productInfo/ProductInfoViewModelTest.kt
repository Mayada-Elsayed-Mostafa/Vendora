package com.example.vendora.ui.screens.productInfo

import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.usecase.products.GetProductByIdUseCase
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProductInfoViewModelTest {

    private lateinit var viewModel: ProductInfoViewModel
    private val getProductByIdUseCase = mockk<GetProductByIdUseCase>()
    private val mockUserPreferences = mockk<UserPreferences>()

    @Before
    fun setup() {
        viewModel = ProductInfoViewModel(getProductByIdUseCase,mockUserPreferences)
    }

    @Test
    fun `onColorSelected updates selectedColor`() {
        viewModel.onColorSelected("Red")
        assertEquals("Red", viewModel.selectedColor.value)
    }

    @Test
    fun `onSizeSelected updates selectedSize`() {
        viewModel.onSizeSelected("M")
        assertEquals("M", viewModel.selectedSize.value)
    }

    @Test
    fun `increaseQuantity increments quantity`() {
        viewModel.increaseQuantity()
        assertEquals(2, viewModel.quantity.value)
    }

    @Test
    fun `decreaseQuantity decrements quantity if greater than 1`() {
        viewModel.increaseQuantity()
        viewModel.decreaseQuantity()
        assertEquals(1, viewModel.quantity.value)
    }

    @Test
    fun `initializeOptions sets first color and size if not already set`() {
        viewModel.initializeOptions(listOf("Red", "Blue"), listOf("S", "M"))
        assertEquals("Red", viewModel.selectedColor.value)
        assertEquals("S", viewModel.selectedSize.value)
    }
}
