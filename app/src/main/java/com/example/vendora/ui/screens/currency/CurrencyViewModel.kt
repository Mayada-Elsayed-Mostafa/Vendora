package com.example.vendora.ui.screens.currency

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.domain.model.currency.CurrencyInfo
import com.example.vendora.domain.model.currency.CurrencyResponse
import com.example.vendora.domain.usecase.currency.GetCurrencyRatesUseCase
import com.example.vendora.domain.usecase.currency.GetCurrencyUseCase
import com.example.vendora.domain.usecase.currency.GetRatesUseCase
import com.example.vendora.domain.usecase.currency.GetSelectedCurrencyUseCase
import com.example.vendora.domain.usecase.currency.SaveCurrencyUseCase
import com.example.vendora.domain.usecase.currency.SaveSelectedCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
    private val getRatesUseCase: GetRatesUseCase,
    private val saveCurrencyUseCase: SaveCurrencyUseCase ,
    private val getCurrencyUseCase: GetCurrencyUseCase ,
    private val saveSelectedCurrencyUseCase: SaveSelectedCurrencyUseCase,
    private val getSelectedCurrencyUseCase: GetSelectedCurrencyUseCase
) : ViewModel(){
    private val _currencies = MutableStateFlow<Result<CurrencyResponse>>(Result.Loading)
    val currencies = _currencies.asStateFlow()

    private val _selectedCurrency = MutableStateFlow("EGP")
    val selectedCurrency = _selectedCurrency.asStateFlow()

    private val _rates = MutableStateFlow<Result<List<CurrencyInfo>>>(Result.Loading)
    val rates = _rates.asStateFlow()

    private val _getChangeRate = MutableStateFlow<Double>(0.0)
    val getChangeRate = _getChangeRate.asStateFlow()

    init {
        //getRates()
        getSelectedCurrency()
        getRateValue()
    }

    fun getCurrency(baseCurrency:String = "EGP"){
        viewModelScope.launch {
            getCurrencyRatesUseCase.invoke(baseCurrency)
                .flowOn(Dispatchers.IO)
                .collect{ result ->
                    _currencies.value = result
                }
        }
    }
    fun getRates(){
        viewModelScope.launch {
            getRatesUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .collect{ result ->
                    _rates.value = result

                    if (result is Result.Success){
                        withContext(Dispatchers.IO){
                            for (item in result.data){
                                saveCurrencyUseCase.invoke(item.code,item.value.toString())
                            }
                        }

                    }
                }
        }
    }

    fun changeSelectedCurrency(currency: String){
        _selectedCurrency.value = currency
        saveSelectedCurrencyUseCase.invoke(currency)
        _getChangeRate.value = getCurrencyUseCase.invoke(currency,"EGP")
    }

     fun getSelectedCurrency():String {
         _selectedCurrency.value = getSelectedCurrencyUseCase.invoke()

         return _selectedCurrency.value
     }

    fun getRateValue() : Double{
        _getChangeRate.value = getCurrencyUseCase.invoke(_selectedCurrency.value,"EGP")
        return _getChangeRate.value
    }
}



fun Double.convertToCurrency(toCurrency: Double ): Double {
    return String.format("%.2f", this * toCurrency).toDouble()
}

fun Double.changeCurrency(context: Context):String {
     val prefs = context.getSharedPreferences("vendora", Context.MODE_PRIVATE)

     val currency = prefs.getString("selected_currency","EGP")
     val rate =  prefs.getString(currency,"1.0")

    return String.format("%.2f", this * rate.toString().toDouble()) + " $currency"
}



