package com.example.vendora.data.repo_implementation

import com.example.vendora.data.local.LocalDataSource
import com.example.vendora.data.remote.CurrencyApiService
import com.example.vendora.domain.model.currency.CurrencyInfo
import com.example.vendora.domain.model.currency.CurrencyResponse
import com.example.vendora.domain.repo_interfaces.CurrencyRepository
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.toList


class CurrencyRepositoryImplTest{

    private lateinit var repository: CurrencyRepository
    private val mockApi = mockk<CurrencyApiService>()
    private val  mockLocalData = mockk<LocalDataSource>(relaxed = true)


    @Before
    fun setup(){
        repository = CurrencyRepositoryImpl(mockApi,mockLocalData)
    }

    @Test
    fun `getCurrency should emit loading then Success` ()= runTest {
        //given
        val fakeResponse = CurrencyResponse(mapOf("EGP" to CurrencyInfo("EGP",50.0)))
        coEvery { mockApi.getCurrency(any(),"EGP") } returns fakeResponse

        //when
        val resultList = repository.getCurrency("EGP").toList()

        //then
        assertEquals(Result.Loading,resultList[0])
        assertEquals(Result.Success(fakeResponse),resultList[1])

    }

    @Test
    fun getCurrency_emitLoading_thenFailure()= runTest{
        //Given
        val fakeResponse  = Exception("Network error")
        coEvery { mockApi.getCurrency(any(),"EGP") } throws  fakeResponse

        //when
        val resultList = repository.getCurrency("EGP").toList()

        //then
        assertEquals(Result.Loading,resultList[0])
        assertEquals(Result.Failure(fakeResponse),resultList[1])
    }

    @Test
    fun getRates_emitLoading_thenSuccess()= runTest{
        //Given
        val currencyInfoList = listOf(CurrencyInfo("USD",1.0),CurrencyInfo("EGP",48.5))
        val fakeResponse = CurrencyResponse(
            data = mapOf(
                "USD" to CurrencyInfo("USD",1.0),
                "EGP" to CurrencyInfo("EGP",48.5),
            ),)

        coEvery { mockApi.getCurrency(any()) } returns fakeResponse

        //when
        val resultList = repository.getRates().toList()

        //Then
        assertEquals(Result.Loading,resultList[0])
        assertEquals(Result.Success(currencyInfoList),resultList[1])
    }

    @Test
    fun saveCurrency_shouldCallLocalDAtaSource (){

        //when
        repository.saveCurrency("USD","45.0")

        //Then
        verify { mockLocalData.saveCurrency("USD","45.0") }
    }



}