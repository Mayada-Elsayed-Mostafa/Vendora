package com.example.vendora.domain.usecase.currency

import com.example.vendora.domain.repo_interfaces.CurrencyRepository
import javax.inject.Inject


class SaveCurrencyUseCase @Inject constructor(private val repository: CurrencyRepository) {
    operator fun invoke (code : String,value: String){
        repository.saveCurrency(code,value)
    }
}

class GetCurrencyUseCase @Inject constructor(private val repository: CurrencyRepository) {
    operator fun invoke (code: String, defaultValue: String) : Double{
        return repository.geTCurrency(code,defaultValue ="1").toDouble()
    }
}



class SaveSelectedCurrencyUseCase @Inject constructor(private val repository: CurrencyRepository) {
    operator fun invoke (code : String){
        repository.saveSelectedCurrency(code)
    }
}

class GetSelectedCurrencyUseCase @Inject constructor(
    private val repo: CurrencyRepository
) {
    operator fun invoke(): String = repo.getSelectedCurrency()
}