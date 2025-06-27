package com.example.vendora.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    }

    suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun isUserLoggedIn(): Boolean {
        val isLoggedIn = context.dataStore.data.map { prefs ->
            prefs[IS_LOGGED_IN]
        }.first()
        return isLoggedIn ?: false
    }

    suspend fun saveUser(id: String, name: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = id
            prefs[USER_NAME] = name
            prefs[USER_EMAIL] = email
        }
    }

    suspend fun getUserId(): String? {
        return context.dataStore.data.map { prefs -> prefs[USER_ID] }.first()
    }

    suspend fun getUserName(): String? {
        return context.dataStore.data.map { prefs -> prefs[USER_NAME] }.first()
    }

    suspend fun getUserEmail(): String? {
        return context.dataStore.data.map { prefs -> prefs[USER_EMAIL] }.first()
    }

    suspend fun clearUser() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

}
