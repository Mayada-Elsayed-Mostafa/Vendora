package com.example.vendora.domain.usecase.category

import com.example.vendora.domain.repo_interfaces.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(){
        repository.getCategories()
    }
}