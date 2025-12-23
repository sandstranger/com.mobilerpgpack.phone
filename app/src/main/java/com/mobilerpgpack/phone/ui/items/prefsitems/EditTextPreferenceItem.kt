package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.KeyboardType
import com.mobilerpgpack.phone.ui.items.EditTextItem
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.flow.Flow
import org.koin.compose.koinInject

@Composable
fun EditTextPreferenceItem(
    title: String,
    value: String,
    key : String = "",
    hint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: ((String) -> Unit)? = null) {
    val preferencesStorage : PreferencesStorage = koinInject()
    val key = rememberSaveable(key) { key }
    EditTextItem(title,value, hint, keyboardType = keyboardType){
        if (key.isNotEmpty()){
            preferencesStorage.setStringValue(key, it)
        }
        onValueChanged?.invoke(it)
    }
}

@Composable
@JvmName("EditTextPreferenceItemFloat")
fun EditTextPreferenceItem(
    title: String,
    value: Float,
    key : String = "",
    hint: String = "",
    onValueChanged: ((Float) -> Unit)? = null){
    val preferencesStorage : PreferencesStorage = koinInject()
    val key = rememberSaveable(key) { key }
    val stringValue = rememberSaveable (value) { value.toString() }
    EditTextPreferenceItem(title, stringValue, "", hint, KeyboardType.Decimal){
        val newValue = it.toFloatOrNull() ?: 0f
        if (key.isNotEmpty()){
            preferencesStorage.setFloatValue(key,newValue)
        }
        onValueChanged?.invoke(newValue)
    }
}

@Composable
@JvmName("EditTextPreferenceItemInt")
fun EditTextPreferenceItem(
    title: String,
    value: Int,
    key : String = "",
    hint: String = "",
    onValueChanged: ((Int) -> Unit)? = null){
    val preferencesStorage : PreferencesStorage = koinInject()
    val key = rememberSaveable(key) { key }
    val stringValue = rememberSaveable (value) { value.toString() }
    EditTextPreferenceItem(title, stringValue, "", hint,KeyboardType.Number){
        val newValue = it.toIntOrNull() ?: 0
        if (key.isNotEmpty()){
            preferencesStorage.setIntValue(key, newValue)
        }
        onValueChanged?.invoke(newValue)
    }
}