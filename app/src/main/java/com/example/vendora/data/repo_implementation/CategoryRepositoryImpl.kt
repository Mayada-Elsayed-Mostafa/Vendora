package com.example.vendora.data.repo_implementation

import com.example.vendora.BuildConfig
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.repo_interfaces.CategoryRepository
import com.example.vendora.utils.wrapper.ProductCategoryBlock
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val remoteSource: RemoteDataSource
) : CategoryRepository {

    private val token = BuildConfig.adminApiAccessToken
    private var categoriesList = mutableListOf<ProductCategoryBlock>()
    val resultFlow = MutableStateFlow<Result<ProductCategoryBlock>>(Result.Loading)

    override suspend fun getCategories() {
        resultFlow.emit(Result.Loading)
        remoteSource.getCategories(token)
            .custom_collections.forEach { category ->
                try {
                    val response = remoteSource.getProductsByBrandId(category.id, token)
                    categoriesList.add(
                        ProductCategoryBlock(
                            categoryTitle = category.handle,
                            products = response.products
                        )
                    )
                } catch (e: Exception) {
                    resultFlow.emit(Result.Failure(e))
                }
            }
        categorizedProductsFactory("women")
    }

    // function to determine which kind of categorized products should be returned.
    override suspend fun categorizedProductsFactory(title: String) {
        categoriesList.forEach {
            if (it.categoryTitle == title) {
                resultFlow.emit(Result.Success(it))
                return@forEach
            }
        }
    }

}