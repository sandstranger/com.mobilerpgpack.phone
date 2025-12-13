package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mobilerpgpack.phone.ui.items.SwitchItem
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.flow.Flow
import org.koin.compose.koinInject

@Composable
fun SwitchPreferenceItem(
    title: String,
    initialValue: Boolean,
    key: String = "",
    enabled: Boolean = true,
    onValueChanged : (Boolean) -> Unit = { } ) {
    val preferencesStorage : PreferencesStorage = koinInject()
    SwitchItem(title, initialValue,enabled) {
        if (key.isNotEmpty()){
            preferencesStorage.setBooleanValue(key, it)
        }
        onValueChanged(it)
    }
}

@Composable
fun SwitchPreferenceItem(
    title: String,
    initialValueFlow: Flow<Boolean>,
    key: String = "",
    enabled: Boolean = true,
    onValueChanged : (Boolean) -> Unit = { } ) {
    val initialValue by initialValueFlow.collectAsState(initial = false)
    SwitchPreferenceItem(title, initialValue, key, enabled, onValueChanged)
}
