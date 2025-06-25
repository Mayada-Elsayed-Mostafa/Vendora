package com.example.vendora.data.repo_implementation

import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.model.product.SingleProduct
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProductRepositoryImplTest {

    private lateinit var repository: ProductRepositoryImpl
    private val remoteSource = mockk<RemoteDataSource>()

    @Before
    fun setup() {
        repository = ProductRepositoryImpl(remoteSource)
    }

    @Test
    fun `getProductById emits Loading then Success`() = runTest {
        // Given
        val productId = 123L
        val fakeProduct = Product(id = productId, title = "Test Product")
        val fakeResponse = SingleProduct(product = fakeProduct)

        coEvery { remoteSource.getProductById(any(), productId) } returns fakeResponse

        // When
        val result = repository.getProductById(productId).toList()

        // Then
        assertTrue(result[0] is Result.Loading)
        assertTrue(result[1] is Result.Success)
        assertEquals(fakeProduct, (result[1] as Result.Success).data)
    }

    @Test
    fun `getProductById emits Loading then Failure when exception thrown`() = runTest {
        // Given
        val productId = 123L
        val exception = RuntimeException("Network error")
        coEvery { remoteSource.getProductById(any(), productId) } throws exception

        // When
        val result = repository.getProductById(productId).toList()

        // Then
        assertTrue(result[0] is Result.Loading)
        assertTrue(result[1] is Result.Failure)
        assertEquals(exception, (result[1] as Result.Failure).exception)
    }
}
