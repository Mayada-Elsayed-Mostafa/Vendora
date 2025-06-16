package com.example.vendora.di

import com.example.vendora.data.repo_implementation.OrdersRepositoryImpl
import com.example.vendora.domain.repo_interfaces.OrdersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OrderModule {

    @Binds
    @Singleton
    abstract fun bindOrderRepository(repository: OrdersRepositoryImpl): OrdersRepository
}