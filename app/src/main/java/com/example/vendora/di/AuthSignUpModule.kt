package com.example.vendora.di

import com.example.vendora.data.repo_implementation.AuthSignUpRepositoryImpl
import com.example.vendora.domain.repo_interfaces.AuthSignUpRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthSignUpModule {

    @Binds
    @Singleton
    abstract fun bindAuthSignUpRepository(
        impl: AuthSignUpRepositoryImpl
    ): AuthSignUpRepository
}