package com.example.vendora.ui.cart_screen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import com.example.vendora.domain.usecase.payment.CreateOrderUseCase
import com.example.vendora.domain.usecase.payment.GetPaymentKeyUseCase
import com.example.vendora.domain.usecase.payment.GetTokenForPayMob
import com.example.vendora.utils.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymobViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenForPayMob,
    private val createOrderUseCase: CreateOrderUseCase,
    private val getPaymentKeyUseCase: GetPaymentKeyUseCase
) : ViewModel(){

    //to get First token
    private val _getTokenState = MutableStateFlow<Result<AuthTokenResponse>>(Result.Loading)
    val getTokenState: StateFlow<Result<AuthTokenResponse>> = _getTokenState.asStateFlow()

    private val _orderState = MutableStateFlow<Result<OrderResponse>>(Result.Loading)
    val orderState = _orderState.asStateFlow()

    private val _paymentKeyState = MutableStateFlow<Result<PaymentKeyResponse>>(Result.Loading)
    val paymentKeyState = _paymentKeyState.asStateFlow()


    fun getTokenForAuthentication (){
        viewModelScope.launch {
            _getTokenState.value= Result.Loading
            getTokenUseCase()
                .flowOn(Dispatchers.IO)
                .collect{
                _getTokenState.value = it
            }
        }
    }

    fun createOrder(orderRequest: OrderRequest){
        viewModelScope.launch {
            _orderState.value = Result.Loading
            createOrderUseCase(orderRequest)
                .flowOn(Dispatchers.IO)
                .collect{
                _orderState.value =it
            }
        }
    }

    fun getPaymentKey (paymentKeyRequest: PaymentKeyRequest){
        viewModelScope.launch {
            getPaymentKeyUseCase(paymentKeyRequest)
                .flowOn(Dispatchers.IO)
                .collect{
                _paymentKeyState.value = it
            }
        }
    }


}