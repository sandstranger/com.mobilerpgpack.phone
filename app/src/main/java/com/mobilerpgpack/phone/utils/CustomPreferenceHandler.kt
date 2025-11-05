package com.mobilerpgpack.phone.utils

import com.github.sproctor.composepreferences.PreferenceHandler
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.datastore.DataStoreSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

@OptIn(ExperimentalSettingsApi::class, ExperimentalSettingsImplementation::class)
class CustomPreferenceHandler (private val settings : DataStoreSettings) : PreferenceHandler, KoinComponent {

    private val scope: CoroutineScope by inject (named(KoinModulesProvider.COROUTINES_SCOPE))

    override fun putString(key: String, value: String) {
        scope.launch {
            settings.putString(key, value)
        }
    }

    override fun putBoolean(key: String, value: Boolean) {
        scope.launch {
            settings.putBoolean(key, value)
        }
    }

    override fun putFloat(key: String, value: Float) {
        scope.launch {
            settings.putFloat(key, value)
        }
    }

    override fun putList(key: String, values: List<String>) {
        scope.launch {
            settings.putString(key, values.joinToString(","))
        }
    }

    override suspend fun getString(key: String): String {
        return settings.getString(key, "")
    }

    override suspend fun getBoolean(key: String): Boolean {
        return settings.getBoolean(key, false)
    }

    override suspend fun getFloat(key: String): Float {
        return settings.getFloat(key, 0f)
    }

    override suspend fun getList(key: String): List<String> {
        return settings.getStringOrNull(key)?.split(",") ?: emptyList()
    }
}