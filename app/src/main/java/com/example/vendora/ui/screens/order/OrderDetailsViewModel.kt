package com.example.vendora.ui.screens.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.data.repo_implementation.OrdersRepositoryImpl
import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.domain.usecase.order.GetSingleOrderUseCase
import com.example.vendora.utils.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel  @Inject constructor(
    private val useCase: GetSingleOrderUseCase
): ViewModel(){

    private var _order = MutableStateFlow<Result<SingleOrderResponse>>(Result.Loading)
    val order = _order.asStateFlow()


    fun collectOrder(orderId: Long){
        viewModelScope.launch {
            useCase.invoke(orderId).collect{ order ->
                _order.value = order
            }
        }
    }
}