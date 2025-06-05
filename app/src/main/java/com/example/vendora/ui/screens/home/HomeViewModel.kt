package com.example.vendora.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.usecase.brands.GetBrandsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.vendora.utils.wrapper.Result

class HomeViewModel(private val getBrandsUseCase: GetBrandsUseCase): ViewModel() {

    private var _brands = MutableStateFlow<Result<BrandsResponse>>(Result.Loading)
    val brands = _brands.asStateFlow()

    fun fetchBrands(){
        viewModelScope.launch(Dispatchers.IO) {
            getBrandsUseCase.invoke().collect{
                _brands.value = it
            }
        }
    }
}

class HomeViewModelFactory(private val getBrandsUseCase: GetBrandsUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(getBrandsUseCase) as T
    }
}