package com.example.vendora.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.vendora.data.local.AddressDao
import com.example.vendora.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context):AppDatabase {
        return Room.databaseBuilder(context,AppDatabase::class.java,"my_db")
            .fallbackToDestructiveMigration(false)
            .build()
    }


    @Provides
    fun provideAddressDao (db:AppDatabase):AddressDao {
        return db.addressDao()
    }


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("vendora", Context.MODE_PRIVATE)
    }

}