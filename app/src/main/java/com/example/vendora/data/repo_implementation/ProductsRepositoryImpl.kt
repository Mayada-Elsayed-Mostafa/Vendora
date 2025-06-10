package com.example.vendora.data.repo_implementation

import com.example.vendora.BuildConfig
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.brands.BrandsResponse
import com.example.vendora.domain.model.brands.SmartCollection
import com.example.vendora.domain.model.category.CategoryResponse
import com.example.vendora.domain.model.product.Products
import com.example.vendora.domain.repo_interfaces.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.vendora.utils.wrapper.Result
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val remoteSource: RemoteDataSource): ProductsRepository {

    private var cachedBrands: BrandsResponse? = null
    private var cachedProducts: Products? = null

    override fun getBrands(): Flow<Result<BrandsResponse>> = flow {
        if (cachedBrands != null){
            emit(Result.Success(cachedBrands!!))
            return@flow
        }

        emit(Result.Loading)
        try {
            val response = remoteSource.getBrands(BuildConfig.adminApiAccessToken)
            cacheBrands(response)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    override fun getProductsByBrandId(brandId: Long): Flow<Result<Products>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteSource.getProductsByBrandId(
                brandId = brandId,
                token = BuildConfig.adminApiAccessToken
            )
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    override fun getProducts(): Flow<Result<Products>> = flow {
        if (cachedProducts != null){
            emit(Result.Success(cachedProducts!!))
            return@flow
        }

        try {
            val response = remoteSource.getProducts(token = BuildConfig.adminApiAccessToken)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    override fun cacheBrands(response: BrandsResponse) {
        cachedBrands = response
    }

    override fun cacheProducts(response: Products) {
        cachedProducts = response
    }

}