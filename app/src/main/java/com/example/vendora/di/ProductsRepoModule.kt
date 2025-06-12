package com.example.vendora.di

import com.example.vendora.data.local.LocalDataSource
import com.example.vendora.data.local.LocalDataSourceImpl
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.data.remote.RemoteDataSourceImpl
import com.example.vendora.data.remote.ShopifyService
import com.example.vendora.data.repo_implementation.CategoryRepositoryImpl
import com.example.vendora.data.repo_implementation.CurrencyRepositoryImpl
import com.example.vendora.data.repo_implementation.DiscountRepositoryImpl
import com.example.vendora.data.repo_implementation.ProductsRepositoryImpl
import com.example.vendora.domain.repo_interfaces.CategoryRepository
import com.example.vendora.domain.repo_interfaces.CurrencyRepository
import com.example.vendora.domain.repo_interfaces.DiscountRepository
import com.example.vendora.domain.repo_interfaces.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductsRepoModule {

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(localDataSource: LocalDataSourceImpl):LocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(remote: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindProductsRepository(repository: ProductsRepositoryImpl): ProductsRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(repository: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindDiscountRepository(repository: DiscountRepositoryImpl): DiscountRepository

    @Binds
    @Singleton
    abstract fun bindCurrencyRepository(repository: CurrencyRepositoryImpl): CurrencyRepository

}