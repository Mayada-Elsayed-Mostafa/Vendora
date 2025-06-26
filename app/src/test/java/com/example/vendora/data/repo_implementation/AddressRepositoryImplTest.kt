package com.example.vendora.data.repo_implementation

import com.example.vendora.data.local.AddressDao
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.address.AddressEntity
import com.example.vendora.utils.wrapper.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class AddressRepositoryImplTest{
    private lateinit var repository: AddressRepositoryImpl
    private val addressDao = mockk<AddressDao>(relaxed = true)
    private val remoteDataSource = mockk<RemoteDataSource>(relaxed = true)

    private lateinit var testAddress :AddressEntity

    @Before
    fun setUp(){
        testAddress  = AddressEntity(
            id = 1,
            email = "mk@gmail.com",
            country = "",
            address = "",
            city = "",
            type = "home",
            phone = "01017485777",
            isDefault = true
        )

        repository = AddressRepositoryImpl(addressDao,remoteDataSource)
    }



    @Test
    fun insertAddress_withDefault_ShouldClear_PrevDefaults()= runTest {
        //given

        coEvery { addressDao.clearDefaultAddress(testAddress.email) } returns Unit
        coEvery { addressDao.insertAddress(testAddress) } returns 1

        //when
        val result = repository.insertAddress(testAddress)

        //Then

        assertEquals(1,result)

    }


    @Test
    fun deleteAddress_callDao_returnReturn1()= runTest {
        //Given
        val addressId= 5
        coEvery { addressDao.deleteAddress(addressId) } returns 1

        //when
        val result = repository.deleteAddress(addressId)

        //then
        coVerify { addressDao.deleteAddress(addressId) }
        assertEquals(1,result)

    }


    @Test
    fun getAllAddress_returnList()= runTest {
        //Given
        val fakeList = listOf(testAddress)
        coEvery { addressDao.getAddressesByEmail(testAddress.email) } returns fakeList

        //when
        val result = repository.getAllAddressesByEmail(testAddress.email)

        //
        coVerify { addressDao.getAddressesByEmail(testAddress.email) }
        assertEquals(Result.Success(fakeList),result)

    }

}