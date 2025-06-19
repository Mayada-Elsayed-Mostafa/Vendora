package com.example.vendora.ui.screens.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.model.order.Item
import com.example.vendora.domain.model.order.LineItemBuild
import com.example.vendora.domain.model.order.OrderPaymentResult
import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.domain.usecase.order.CreateShopifyOrderUserCase
import com.example.vendora.domain.usecase.order.GetOrderPaymentResultUseCase
import com.example.vendora.domain.usecase.payment.CreateOrderUseCase
import com.example.vendora.utils.wrapper.Result
import com.example.vendora.utils.wrapper.order_builder.CreateOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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

    fun getOrderResult(
        orderId: Int,
        token: String
    ) {
        viewModelScope.launch {
            paymentResultUseCase.invoke(orderId, token)
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

    fun createOrder(result: OrderPaymentResult) {
        viewModelScope.launch(Dispatchers.IO) {
            val order = CreateOrder()
            val requestBody =
                order.email(userPreferences.getUserEmail() ?: "zeyadmamoun952@gmail.com")
                    .financialStatus("paid")
                    .currency(result.currency)
                    .lineItems(createLineItems(result.items))
                    .build()

            createShopifyOrderUserCase.invoke(requestBody).collect{ creationResult ->
                _uiState.update {
                    it.copy(
                        orderCreationResult = creationResult
                    )
                }
            }
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