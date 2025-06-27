import com.example.vendora.domain.model.order.UserOrdersResponse
import com.example.vendora.domain.usecase.order.GetOrdersByEmailUseCase
import com.example.vendora.ui.screens.order.OrderViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class OrderViewModelTest {

    private val mockUseCase = mockk<GetOrdersByEmailUseCase>()
    private val mockFirebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val mockFirebaseUser = mockk<FirebaseUser>()
    private lateinit var viewModel: OrderViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { mockFirebaseAuth.currentUser } returns mockFirebaseUser
        every { mockFirebaseUser.email } returns "test@example.com"

        viewModel = OrderViewModel(mockUseCase, mockFirebaseAuth)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun collectOrders_shouldEmitSuccessResult() = runTest {
        // Given
        val fakeResponse = mockk<UserOrdersResponse>(relaxed = true)
        val flow = flowOf(Result.Loading, Result.Success(fakeResponse))

        coEvery { mockUseCase.invoke("test@example.com") } returns flow

        // When
        viewModel.collectOrders()
        advanceUntilIdle()

        // Then
        val result = viewModel.orders.value
        assertTrue(result is Result.Success)
        assertEquals(fakeResponse, (result as Result.Success).data)
    }
}