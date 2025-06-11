package com.example.vendora.ui.screens.currency

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.domain.model.currency.CurrencyInfo
import com.example.vendora.domain.model.currency.CurrencyResponse
import com.example.vendora.domain.usecase.currency.GetCurrencyRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@HiltViewModel
class CurrencyViewModel @Inject constructor(private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase) : ViewModel(){
    private val _currencies = MutableStateFlow<Result<CurrencyResponse>>(Result.Loading)
    val currencies = _currencies.asStateFlow()

    private val _selectedCurrency = MutableStateFlow("EGP")
    val selectedCurrency = _selectedCurrency.asStateFlow()

    private val _rates = MutableStateFlow<Map<String, Double>>(emptyMap())
    val rates = _rates.asStateFlow()

    fun getCurrency(baseCurrency:String = "EGP"){
        viewModelScope.launch {
            getCurrencyRatesUseCase.invoke(baseCurrency)
                .flowOn(Dispatchers.IO)
                .collect{ result ->
                    _currencies.value = result
                    println("$$$$$$$$$$$$"+_currencies.value)
                    if(result is Result.Success){
                        val ratesMap = result.data.data.mapValues { it.value.value }
                        _rates.value = ratesMap
                    }
                }
        }
    }

    fun changeSelectedCurrency(currency: String){
        _selectedCurrency.value = currency
    }
}



fun Double.convertToCurrency(currencyViewModel: CurrencyViewModel): String{
    val selected = currencyViewModel.selectedCurrency.value
    val rates = currencyViewModel.rates.value

    val egpRate = rates["EGP"] ?: 1.0
    val selectedRate = rates[selected] ?: 1.0

    val result = this / egpRate * selectedRate
    return String.format("%.2f %s", result, selected)
}