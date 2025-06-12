package com.example.vendora.ui.screens.discount.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.domain.model.discount.DiscountCode
import com.example.vendora.domain.usecase.discount.GetDiscountCodesUseCase
import com.example.vendora.utils.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscountViewModel @Inject constructor(private val discountCodesUseCase: GetDiscountCodesUseCase) : ViewModel(){
    private val _discountCodes = MutableStateFlow<Result<List<DiscountCode>>>(Result.Loading)
    val discountCodes  = _discountCodes.asStateFlow()

    fun getDiscountCode(){
        viewModelScope.launch {
            discountCodesUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .collect{
                    _discountCodes.value = it
                }
        }
    }
}