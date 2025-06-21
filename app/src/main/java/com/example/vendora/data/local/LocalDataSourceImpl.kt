package com.example.vendora.data.local

import android.content.SharedPreferences
import com.example.vendora.domain.model.address.AddressEntity
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val addressDao: AddressDao, private val sharedPreferences: SharedPreferences) : LocalDataSource{
    override suspend fun getAllAddresses(): List<AddressEntity> {
        return addressDao.getAllAddresses()
    }

    override suspend fun insertAddress(addressEntity: AddressEntity): Long {
        return addressDao.insertAddress(addressEntity)
    }

    override suspend fun deleteAddress(addressId: Int): Int {
        return addressDao.deleteAddress(addressId)
    }

    override suspend fun clearDefaultAddress(email: String) {
        return addressDao.clearDefaultAddress(email)
    }

    override suspend fun getAddressesByEmail(email: String): List<AddressEntity> {
        return addressDao.getAddressesByEmail(email)
    }

    override fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: "EGP"
    }

    override fun saveCurrency(code: String, value: String) {
        sharedPreferences.edit().putString(code, value).apply()
    }

    override fun geTCurrency(code: String, defaultValue: String): String {
        return sharedPreferences.getString(code, defaultValue) ?: "EGP"
    }

    override fun saveSelectedCurrency(code: String) {
        putString("selected_currency", code)
    }

    override fun getSelectedCurrency(): String {
        return getString("selected_currency", "EGP")
    }
}