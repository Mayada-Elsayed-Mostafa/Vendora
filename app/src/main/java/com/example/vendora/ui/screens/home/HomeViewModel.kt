package com.example.vendora.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.usecase.brands.GetBrandsUseCase
import com.example.vendora.domain.usecase.search.SearchProductsUseCase
import com.example.vendora.utils.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBrandsUseCase: GetBrandsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private var _brands = MutableStateFlow<Result<BrandsResponse>>(Result.Loading)
    val brands = _brands.asStateFlow()

    fun fetchBrands() {
        viewModelScope.launch {
            getBrandsUseCase()
                .flowOn(Dispatchers.IO)
                .collect {
                    _brands.value = it
                }
        }
    }
}