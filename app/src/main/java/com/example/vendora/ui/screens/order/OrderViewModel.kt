package com.example.vendora.ui.screens.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.model.order.UserOrdersResponse
import com.example.vendora.domain.usecase.order.GetOrdersByEmailUseCase
import com.example.vendora.utils.wrapper.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
//        Log.d(TAG,auth?.email.toString())
        viewModelScope.launch {
            orderUseCase.invoke(getCustomerEmail().ifEmpty { return@launch }).collect { result ->
                _orders.value = result
            }
        }
    }

    private fun getCustomerEmail(): String = runBlocking {
        userPreferences.getUserEmail() ?: ""
    }
}