package com.example.vendora.di

import com.example.vendora.data.repo_implementation.PaymobRepositoryImpl
import com.example.vendora.domain.repo_interfaces.PaymobRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PaymobRepoModule{

    @Binds
    @Singleton
    abstract fun bindPaymobRepository(repository: PaymobRepositoryImpl): PaymobRepository
}