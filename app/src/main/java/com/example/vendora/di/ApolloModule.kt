package com.example.vendora.di

import com.apollographql.apollo.ApolloClient
import com.example.vendora.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://mad45-ism-and1.myshopify.com/api/2025-04/graphql.json")
            .addHttpHeader("X-Shopify-Storefront-Access-Token", "702b21e78cf63f5083b1a9317c624bf7")
            .build()
    }
}