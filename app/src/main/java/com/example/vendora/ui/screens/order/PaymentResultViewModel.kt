package com.example.vendora.ui.screens.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.model.order.Item
import com.example.vendora.domain.model.order.LineItemBuild
import com.example.vendora.domain.model.order.OrderPaymentResult
import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.domain.usecase.order.CreateShopifyOrderUserCase
import com.example.vendora.domain.usecase.order.GetOrderPaymentResultUseCase
import com.example.vendora.utils.wrapper.Result
import com.example.vendora.utils.wrapper.order_builder.CreateOrder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentResultViewModel @Inject constructor(
    private val createShopifyOrderUserCase: CreateShopifyOrderUserCase,
    private val paymentResultUseCase: GetOrderPaymentResultUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private var _uiState = MutableStateFlow(PaymentResultUiState())
    val uiState = _uiState.asStateFlow()

    val TAG = "PaymentResultViewModel"

    fun getOrderResult(
        orderId: Int,
        token: String
    ) {
        val bearerToken = "Bearer $token"
        viewModelScope.launch {
            paymentResultUseCase.invoke(orderId, bearerToken)
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    _uiState.update {
                        it.copy(
                            result = result
                        )
                    }
                }
        }
    }

    suspend fun createOrder(result: OrderPaymentResult, finStatus: String = "paid") {

        val order = CreateOrder()
        val requestBody =
            order.email(userPreferences.getUserEmail() ?: "")
                .financialStatus(if (finStatus == "Cash") "pending" else "paid")
                .currency(result.currency)
                .lineItems(createLineItems(result.items))
                .build()

        Log.d("CashOnDelivery",requestBody.toString())

        createShopifyOrderUserCase.invoke(requestBody).collect { creationResult ->
            Log.d(TAG, creationResult.toString())
            if (creationResult is Result.Success) {
                _uiState.update {
                    it.copy(
                        orderCreationResult = creationResult
                    )
                }
            }
            return@collect
        }
    }

    private fun createLineItems(items: List<Item>): List<LineItemBuild> {
        return items.map { item ->
            LineItemBuild(
                title = item.name,
                price = item.amount_cents.toDouble(),
                quantity = item.quantity
            )
        }.toList()
    }
}

data class PaymentResultUiState(
    val result: Result<OrderPaymentResult> = Result.Loading,
    val orderCreationResult: Result<SingleOrderResponse> = Result.Loading
)