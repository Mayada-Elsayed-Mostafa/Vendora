package com.example.vendora.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.usecase.search.SearchProductsUseCase
import com.example.vendora.utils.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults =
        MutableStateFlow<Result<List<Product>>>(Result.Success(emptyList()))
    val searchResults = _searchResults.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.length >= 2) {
                        searchProductsUseCase(query)
                            .flowOn(Dispatchers.IO)
                    } else {
                        flowOf(Result.Success(emptyList()))
                    }
                }

                .catch { e ->
                    _searchResults.value = Result.Failure(e)
                }
                .collectLatest { result ->
                    Log.d("searchProducts", "searchProducts: $result")
                    _searchResults.value = result
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
