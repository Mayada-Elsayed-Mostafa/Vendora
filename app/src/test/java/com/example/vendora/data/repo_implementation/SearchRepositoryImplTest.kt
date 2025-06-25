package com.example.vendora.data.repo_implementation

import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.model.product.Products
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SearchRepositoryImplTest {

    private lateinit var repository: SearchRepositoryImpl
    private val remoteSource = mockk<RemoteDataSource>()

    @Before
    fun setup() {
        repository = SearchRepositoryImpl(remoteSource)
    }

    @Test
    fun `searchProducts emits Loading then Success with filtered results`() = runTest {
        // Given
        val query = "Shirt"
        val allProducts = listOf(
            Product(title = "Red Shirt"),
            Product(title = "Blue Jeans"),
            Product(title = "Green Shirt")
        )

        coEvery { remoteSource.searchProducts(any(), any()) } returns Products(products = allProducts)

        // When
        val result = repository.searchProducts(query).toList()

        // Then
        assert(result[0] is Result.Loading)
        assert(result[1] is Result.Success)
        val data = (result[1] as Result.Success).data
        Assert.assertEquals(2, data.size)
        assert(data.all { it.title.contains(query, ignoreCase = true) })
    }

    @Test
    fun `searchProducts emits Loading then Failure when exception is thrown`() = runTest {
        // Given
        val query = "Shirt"
        val exception = RuntimeException("Network error")

        coEvery { remoteSource.searchProducts(any(), any()) } throws exception

        // When
        val result = repository.searchProducts(query).toList()

        // Then
        assert(result[0] is Result.Loading)
        assert(result[1] is Result.Failure)
        Assert.assertEquals(exception, (result[1] as Result.Failure).exception)
    }
}
