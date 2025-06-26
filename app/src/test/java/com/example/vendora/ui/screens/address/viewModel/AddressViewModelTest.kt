package com.example.vendora.ui.screens.address.viewModel

import com.example.vendora.domain.model.address.AddressEntity
import com.example.vendora.domain.model.address.Country
import com.example.vendora.domain.model.address.CountryResponse
import com.example.vendora.domain.model.address.Province
import com.example.vendora.domain.usecase.addresses.DeleteAddressUseCase
import com.example.vendora.domain.usecase.addresses.GetAllAddressesByEmailUseCase
import com.example.vendora.domain.usecase.addresses.GetAllAddressesUseCase
import com.example.vendora.domain.usecase.addresses.GetCountryByIdUseCase
import com.example.vendora.domain.usecase.addresses.InsertAddressUseCase
import com.example.vendora.utils.wrapper.Result
import com.google.firebase.auth.FirebaseAuth
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class AddressViewModelTest{
    private lateinit var viewModel: AddressViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val getAllAddressesUseCase = mockk<GetAllAddressesUseCase>(relaxed = true)
    private val insertAddressUseCase = mockk<InsertAddressUseCase>(relaxed = true)
    private val deleteAddressUseCase = mockk<DeleteAddressUseCase>(relaxed = true)
    private val getCountryByIdUseCase = mockk<GetCountryByIdUseCase>(relaxed = true)
    private val getAllAddressesByEmailUseCase = mockk<GetAllAddressesByEmailUseCase>(relaxed = true)
    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)

    @Before
    fun setUp(){
       Dispatchers.setMain(testDispatcher)
        val expectedResponse = CountryResponse(
            country = Country( id = 719296200935,name = "Egypt", code = "EGP", provinces = listOf(
                Province(name = "Cairo", code = "EGP", id = 1010),
            )),

            )

        coEvery { getCountryByIdUseCase.invoke(719296200935) } returns flowOf(Result.Success(expectedResponse))

       viewModel = AddressViewModel(
           getAllAddressesUseCase,
           insertAddressUseCase,
           deleteAddressUseCase,
           getCountryByIdUseCase,
           getAllAddressesByEmailUseCase,
           firebaseAuth
       )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun insertAddress_shouldInsertAddAnd_emitSuccessMessage() = runTest {
        //Given
        val address = AddressEntity(city = "fayoum", address = "sfsf", type = "home", country = "egpyt", email = "mk@gmail.com", phone = "01017485777")

        coEvery { insertAddressUseCase.invoke(address) } returns 1
        coEvery { getAllAddressesByEmailUseCase.invoke(any()) } returns Result.Success(listOf(address))

        //when
        viewModel.insertAddress(address)
        advanceUntilIdle()

        //then
        assertEquals("Address added successfully", viewModel.message.value)
        assert(viewModel.address.value is Result.Success)

    }

    @Test
    fun `deleteAddress when delete add return Address deleted`() = runTest {
        //given
        val addressId = 5
        val email = "test@gmail.com"
        val fakeAdd = listOf(AddressEntity(id = 5,city = "fayoum", address = "sfsf", type = "home", country = "egpyt", email = "mk@gmail.com", phone = "01017485777"))

        coEvery { deleteAddressUseCase.invoke(addressId) } returns 1
        coEvery { getAllAddressesByEmailUseCase.invoke(email) } returns Result.Success(fakeAdd)

        //when
        viewModel.deleteAddress(addressId)
        advanceUntilIdle()

        //then
        assertEquals("Address deleted", viewModel.message.value)

    }

    @Test
    fun `getCountry should update provinces state with success`() = runTest {
        // given

        // when
        viewModel.getCountry()
        advanceUntilIdle()

        // then
        val state = viewModel.provinces.value
        assert(state is Result.Success)


    }

}