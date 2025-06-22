package com.example.vendora.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vendora.domain.model.address.AddressEntity


@Database(entities = [AddressEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun addressDao(): AddressDao
}