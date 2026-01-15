package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import com.mobilerpgpack.phone.ui.items.SwitchItem
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.compose.koinInject

@Composable
fun SwitchPreferenceItem(
    title: String,
    initialValue: Boolean,
    key: String = "",
    enabled: Boolean = true,
    onValueChanged : (Boolean) -> Unit = { } ) {
    val preferencesStorage : PreferencesStorage = koinInject()
    val key = rememberSaveable(key) { key }
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
    initialValue: LiveData<Boolean>,
    key: String = "",
    enabled: Boolean = true,
    onValueChanged : (Boolean) -> Unit = { } ) {
    val liveDataState = initialValue.getComposableValue()
    SwitchPreferenceItem(title, liveDataState, key, enabled, onValueChanged)
}