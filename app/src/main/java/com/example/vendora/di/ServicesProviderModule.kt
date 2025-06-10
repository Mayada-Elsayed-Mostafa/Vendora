package com.example.vendora.di

import com.example.vendora.data.remote.PaymobService
import com.example.vendora.data.remote.RemoteDataSourceImpl
import com.example.vendora.data.remote.ShopifyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ShopifyRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PaymobRetrofit

@Module
@InstallIn(SingletonComponent::class)
class ServicesProviderModule {

    @ShopifyRetrofit
    @Provides
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(RemoteDataSourceImpl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideShopifyService(@ShopifyRetrofit retrofit: Retrofit): ShopifyService{
        return retrofit.create(ShopifyService::class.java)
    }


    @PaymobRetrofit
    @Provides
    fun providePaymobRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://accept.paymob.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providePaymobService(@PaymobRetrofit retrofit: Retrofit): PaymobService {
        return retrofit.create(PaymobService::class.java)
    }



}