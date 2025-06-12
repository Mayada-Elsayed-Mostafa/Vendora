package com.example.vendora.di

import com.example.vendora.data.repo_implementation.AddressRepositoryImpl
import com.example.vendora.domain.repo_interfaces.AddressRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AddressModule {

    @Binds
    @Singleton
    abstract fun bindAddressRepository(
        impl: AddressRepositoryImpl
    ): AddressRepository
}