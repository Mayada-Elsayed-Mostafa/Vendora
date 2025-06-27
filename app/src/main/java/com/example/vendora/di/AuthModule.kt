package com.example.vendora.di

import com.example.vendora.data.repo_implementation.FirebaseAuthRepository
import com.example.vendora.domain.repo_interfaces.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepository
    ): AuthRepository
}
