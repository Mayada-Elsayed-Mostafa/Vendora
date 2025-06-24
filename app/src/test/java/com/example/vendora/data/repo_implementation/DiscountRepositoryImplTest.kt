package com.example.vendora.data.repo_implementation

import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.discount.DiscountCode
import com.example.vendora.domain.repo_interfaces.DiscountRepository
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class DiscountRepositoryImplTest{

    private lateinit var repository: DiscountRepository
    private val mockApi = mockk<RemoteDataSource>()

    @Before
    fun setup(){
        repository = DiscountRepositoryImpl(mockApi)
    }

    @Test
    fun fetchDiscountCodes_emitLoading_thenSuccess()= runTest{
        //Given
        val fakeResponse = listOf(
            DiscountCode(id = 1, code = "MKA", price_rule_id = 1000, created_at = "", usage_count = 0),
            DiscountCode(id = 2, code = "MAD45", price_rule_id = 1000, created_at = "", usage_count = 0)
            )

        coEvery { mockApi.getDiscountCodes(any()) } returns fakeResponse

        //when
        val result = repository.fetchDiscountCodes().toList()

        //Then
        assertEquals(Result.Loading,result[0])
        assertEquals(Result.Success(fakeResponse),result[1])

    }

    @Test
    fun fetchDiscountCodes_emitLoading_thenFailure()= runTest{
        //Given
        val fakeResponse = Exception("Server error")

        coEvery { mockApi.getDiscountCodes(any()) } throws  fakeResponse

        //when
        val result = repository.fetchDiscountCodes().toList()

        //Then
        assertEquals(Result.Loading,result[0])
        assertEquals(Result.Failure(fakeResponse),result[1])

    }

}