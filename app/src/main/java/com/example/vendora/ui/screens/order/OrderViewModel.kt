package com.example.vendora.ui.screens.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.model.order.UserOrdersResponse
import com.example.vendora.domain.usecase.order.GetOrdersByEmailUseCase
import com.example.vendora.utils.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderUseCase: GetOrdersByEmailUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val TAG = "OrderViewModel"

    private var _orders = MutableStateFlow<Result<UserOrdersResponse>>(Result.Loading)
    val orders = _orders.asStateFlow()

    fun collectOrders() {
        viewModelScope.launch {
            orderUseCase.invoke(getCustomerEmail()).collect{ result ->
                _orders.value = result
            }
        }
    }

    private suspend fun getCustomerEmail(): String{
        return userPreferences.getUserEmail() ?: "zeyadmamoun952@gmail.com"
    }
}