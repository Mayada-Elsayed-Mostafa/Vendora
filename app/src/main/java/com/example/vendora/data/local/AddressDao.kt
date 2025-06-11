package com.example.vendora.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vendora.domain.model.address.AddressEntity

@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity): Long

    @Query("SELECT * FROM Address")
    suspend fun getAllAddresses() : List<AddressEntity>

    @Query("UPDATE Address SET isDefault = 0")
    suspend fun clearDefaultAddress()

    @Query("DELETE FROM Address WHERE id = :addressId")
    suspend fun deleteAddress(addressId: Int): Int
}