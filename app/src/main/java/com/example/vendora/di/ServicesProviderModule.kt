package com.example.vendora.di

import com.example.vendora.data.remote.RemoteDataSourceImpl
import com.example.vendora.data.remote.ShopifyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ServicesProviderModule {

    @Provides
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(RemoteDataSourceImpl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideShopifyService(retrofit: Retrofit): ShopifyService{
        return retrofit.create(ShopifyService::class.java)
    }

}