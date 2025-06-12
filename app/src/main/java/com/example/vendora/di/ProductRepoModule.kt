package com.example.vendora.di


import com.example.vendora.data.repo_implementation.ProductRepositoryImpl
import com.example.vendora.domain.repo_interfaces.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductRepoModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(repository: ProductRepositoryImpl): ProductRepository
}
