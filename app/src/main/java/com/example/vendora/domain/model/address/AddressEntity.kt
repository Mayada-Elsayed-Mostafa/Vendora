package com.example.vendora.domain.model.address

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Address")
data class AddressEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val city: String,
    val country: String,
    val address: String,
    val type: String,
    val phone: String,
    val isDefault: Boolean = false
)

