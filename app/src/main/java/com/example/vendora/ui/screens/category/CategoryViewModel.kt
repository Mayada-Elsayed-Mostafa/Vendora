package com.example.vendora.ui.screens.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.data.local.UserPreferences
import com.example.vendora.data.repo_implementation.CategoryRepositoryImpl
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.repo_interfaces.CategoryRepository
import com.example.vendora.domain.usecase.category.GetCategoriesUseCase
import com.example.vendora.utils.wrapper.ProductCategoryBlock
import com.example.vendora.utils.wrapper.Result
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryUiState(
    val categoryResult: Result<ProductCategoryBlock> = Result.Loading,
    var filteredProducts: List<Product> = emptyList(),
    val subCategory: String = "women",
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val repository: CategoryRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private var _uiState = MutableStateFlow(CategoryUiState())
    val uiState = _uiState.asStateFlow()

    private var _isGuestMode = MutableStateFlow(true)
    val isGuestMode = _isGuestMode.asStateFlow()

    init {
        startCollection()
    }

    fun startCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            _isGuestMode.value = !userPreferences.isUserLoggedIn()
            getCategoriesUseCase.invoke()
        }
    }

    fun collectCategories() {
        viewModelScope.launch {
            (repository as CategoryRepositoryImpl).resultFlow.collect { result ->
                Log.d("Category", result.toString())
                _uiState.update {
                    it.copy(
                        categoryResult = result,
                        filteredProducts = if (result is Result.Success) result.data.products else emptyList(),
                    )
                }
            }
        }
    }

    fun onCategoryChanged(title: String) {
        _uiState.update {
            it.copy(
                subCategory = title
            )
        }
        viewModelScope.launch {
            repository.categorizedProductsFactory(title)
        }
    }

    fun applySubCategory(productType: String) {

        if (productType.isEmpty()) {
            _uiState.update {
                it.copy(
                    filteredProducts = (it.categoryResult as Result.Success).data.products
                )
            }
            return
        }
        val filteredList = (_uiState.value.categoryResult as Result.Success)
            .data
            .products
            .filter { it.product_type == productType }

        _uiState.update {
            it.copy(
                filteredProducts = filteredList
            )
        }
    }
}