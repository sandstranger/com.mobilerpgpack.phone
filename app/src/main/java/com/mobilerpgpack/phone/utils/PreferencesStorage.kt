package com.mobilerpgpack.phone.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences_storage")

object PreferencesStorage {
    private val displayInSafeAreaPrefsKey = booleanPreferencesKey("display_in_safe_area")

    fun getDisplayInSafeAreaValue(context: Context): Flow<Boolean?> {
        return context.dataStore.data.map { preferences ->
            preferences[displayInSafeAreaPrefsKey] ?: false
        }
    }

    suspend fun setDisplayInSafeAreaValue(context: Context, valueToSave : Boolean) {
        context.dataStore.edit { preferences ->
            preferences[displayInSafeAreaPrefsKey] = valueToSave
        }
    }
}