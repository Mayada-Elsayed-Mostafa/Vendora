package com.example.vendora.data.repo_implementation

import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.model.brands.SmartCollection
import com.example.vendora.domain.repo_interfaces.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ProductsRepositoryImpl(private val remoteSource: RemoteDataSource): ProductsRepository {
    override fun getBrands(): Flow<List<SmartCollection>> = flow {
        emit(remoteSource.getBrands().smart_collections)
    }.catch {  }
}