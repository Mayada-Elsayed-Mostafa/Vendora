package com.example.vendora.data.repo_implementation

import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.product.Products
import com.example.vendora.domain.repo_interfaces.ProductsRepository
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test

class ProductsRepositoryImplTest {

    private val mockRemote: RemoteDataSource = mockk<RemoteDataSource>()
    private lateinit var repository: ProductsRepository

    @Before
    fun setup() {
        repository = ProductsRepositoryImpl(mockRemote)
    }

    @Test
    fun getBrands_twoEmissions_emitLoadingThenSuccess() = runTest {
        //Given
        val secFakeResponse = BrandsResponse(smart_collections = emptyList())

        //When
        // here we use any() instead of the real shopify token in the repository that is passed in the function getBrands().
        coEvery { mockRemote.getBrands(any()) } returns secFakeResponse
        val result = repository.getBrands().toList()

        //Then
        assertThat(result.size, `is`(2))
        assertThat(result[0], `is`(Result.Loading))
        assertThat(result[1], `is`(Result.Success(secFakeResponse)))
    }

    @Test
    fun getProductsByBrandId_twoEmissions_emitLoadingThenSuccess() = runTest {

        //Given
        val fakeBrandId: Long = 123456
        val fakeResponse = mockk<Products>(relaxed = true)

        //When
        // here we use any() instead of the real shopify token in the repository that is passed in the function getBrands().
        coEvery { mockRemote.getProductsByBrandId(fakeBrandId, any()) } returns fakeResponse
        val result = repository.getProductsByBrandId(fakeBrandId).toList()

        //Then
        assertThat(result.size, `is`(2))
        assertThat(result[0], `is`(Result.Loading))
        assertThat(result[1], `is`(Result.Success(fakeResponse)))
    }

    @Test
    fun getProductsByBrandIdWhenPassingWrongToken_twoEmissions_emitLoadingThenFailure() = runTest {

        //Given
        val fakeBrandId: Long = 123456
        val fakeNonValidToken = "123456"
        val fakeResponse = mockk<Products>(relaxed = true)

        //When we pass non-valid token
        // here we use any() instead of the real shopify token in the repository that is passed in the function getBrands().
        coEvery {
            mockRemote.getProductsByBrandId(
                fakeBrandId,
                fakeNonValidToken
            )
        } throws Exception("Invalid token")
        val result = repository.getProductsByBrandId(fakeBrandId).toList()

        //Then return class of type Result.Failure
        assertThat(result.size, `is`(2))
        assertThat(result[0], `is`(Result.Loading))
        assertThat(result[1], `is`(instanceOf(Result.Failure::class.java)))
    }
}