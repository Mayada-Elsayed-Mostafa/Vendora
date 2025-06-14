package com.example.vendora.di

import com.example.vendora.data.repo_implementation.SearchRepositoryImpl
import com.example.vendora.domain.repo_interfaces.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchRepoModule {
    @Binds
    abstract fun bindSearchRepository(
        impl: SearchRepositoryImpl
    ): SearchRepository
}