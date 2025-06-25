package com.example.vendora.data.repo_implementation

import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.category.CategoryResponse
import com.example.vendora.domain.model.category.CustomCollection
import com.example.vendora.domain.model.product.Products
import com.example.vendora.domain.repo_interfaces.CategoryRepository
import com.example.vendora.type.Product
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CategoryRepositoryImplTest {

    private val mockRemote: RemoteDataSource = mockk<RemoteDataSource>()
    private lateinit var repository: CategoryRepository

    @Before
    fun setup() {
        repository = CategoryRepositoryImpl(mockRemote)
    }

    @Test
    fun getCategories_callingGetCategory_getProductsByBrandIdShouldBeCalledFourTimes() = runTest{
        //Given if the endpoint return four categories
        val fakeResponse = CategoryResponse(
            custom_collections = listOf(
                mockk<CustomCollection>(relaxed = true),
                mockk<CustomCollection>(relaxed = true),
                mockk<CustomCollection>(relaxed = true),
                mockk<CustomCollection>(relaxed = true)
            )
        )

        //when repository call getCategories()
        coEvery { mockRemote.getCategories(any()) } returns fakeResponse
        repository.getCategories()

        // then getCategories get the products for every category and get the data four times.
        coVerify(exactly = 4) { mockRemote.getProductsByBrandId(any(),any()) }
    }

    @Test
    fun getCategories_CallingCategorizedProductsFactory() = runTest{
        //Given if the endpoint return four categories
        val fakeResponse = CategoryResponse(
            custom_collections = listOf(
                mockk<CustomCollection>(relaxed = true),
                mockk<CustomCollection>(relaxed = true),
                mockk<CustomCollection>(relaxed = true),
                mockk<CustomCollection>(relaxed = true)
            )
        )
        val fakeProductsList = mockk<Products>()
        val repository = spyk(CategoryRepositoryImpl(mockRemote))

        //when repository call getCategories()
        coEvery { mockRemote.getCategories(any()) } returns fakeResponse
        coEvery { mockRemote.getProductsByBrandId(any(),any()) } returns fakeProductsList
        repository.getCategories()

        // Then getCategories call categorizedProductsFactory()
        coVerify { repository.categorizedProductsFactory(any()) }
    }
}