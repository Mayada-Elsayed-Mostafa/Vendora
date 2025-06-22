package com.example.vendora.di

import com.example.vendora.data.remote.AddressService
import com.example.vendora.data.remote.CurrencyApiService
import com.example.vendora.data.remote.OrderService
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

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CurrencyRetrofit

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


    @CurrencyRetrofit
    @Provides
    fun provideCurrencyRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.currencyapi.com/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideCurrencyService(@CurrencyRetrofit retrofit: Retrofit): CurrencyApiService {
        return retrofit.create(CurrencyApiService::class.java)
    }

    @Provides
    fun provideOrderService(@ShopifyRetrofit retrofit: Retrofit): OrderService{
        return retrofit.create(OrderService::class.java)
    }

    @Provides
    fun provideAddressService(@ShopifyRetrofit retrofit: Retrofit): AddressService {
        return retrofit.create(AddressService::class.java)
    }

}