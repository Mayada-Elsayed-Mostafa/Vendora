package com.example.vendora.ui.screens.order

import androidx.lifecycle.ViewModel
import com.example.vendora.domain.usecase.payment.CreateOrderUseCase
import com.example.vendora.utils.wrapper.order_builder.CreateOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentResultViewModel @Inject constructor(
    private val useCase: CreateOrderUseCase
): ViewModel() {

    fun getOrderResult(
        orderId: Long,
        token: String
    ){
        //TODO collect the result here.
    }

    fun createOrder(){
        val order = CreateOrder()
        order
            .email("zeyadmamoun952@gmail.com")
            .financialStatus("pending")
            .currency("EGP")
            .build()

        println(order.toString())
    }
}