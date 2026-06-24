package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.LiveData
import com.mobilerpgpack.phone.ui.items.EditTextItem
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.compose.koinInject

@Composable
@JvmName("EditTextPreferenceItemLiveDataString")
fun EditTextPreferenceItem(
    title: String,
    value: LiveData<String>,
    key : String = "",
    hint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: ((String) -> Unit)? = null) {
    EditTextPreferenceItem(title, value.getComposableValue(), key, hint, keyboardType, onValueChanged)
}

@Composable
@JvmName("EditTextPreferenceItemLiveDataFloat")
fun EditTextPreferenceItem(
    title: String,
    value: LiveData<Float>,
    key : String = "",
    hint: String = "",
    onValueChanged: ((Float) -> Unit)? = null){
    val liveDataState = value.getComposableValue()
    EditTextPreferenceItem(title, liveDataState, key, hint, onValueChanged)
}

@Composable
@JvmName("EditTextPreferenceItemLiveDataInt")
fun EditTextPreferenceItem(
    title: String,
    value: LiveData<Int>,
    key : String = "",
    hint: String = "",
    onValueChanged: ((Int) -> Unit)? = null){
    val liveDataState = value.getComposableValue()
    EditTextPreferenceItem(title, liveDataState,key, hint, onValueChanged)
}

@Composable
@JvmName("EditTextPreferenceItemLiveDataLong")
fun EditTextPreferenceItem(
    title: String,
    value: LiveData<Long>,
    key : String = "",
    hint: String = "",
    onValueChanged: ((Long) -> Unit)? = null){
    val liveDataState = value.getComposableValue()
    EditTextPreferenceItem(title, liveDataState,key, hint, onValueChanged)
}

@Composable
@JvmName("EditTextPreferenceItemInt")
private fun EditTextPreferenceItem(
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

@Composable
@JvmName("EditTextPreferenceItemLong")
private fun EditTextPreferenceItem(
    title: String,
    value: Long,
    key : String = "",
    hint: String = "",
    onValueChanged: ((Long) -> Unit)? = null){
    val preferencesStorage : PreferencesStorage = koinInject()
    val key = rememberSaveable(key) { key }
    val stringValue = rememberSaveable (value) { value.toString() }
    EditTextPreferenceItem(title, stringValue, "", hint,KeyboardType.Number){
        val newValue = it.toLongOrNull() ?: 0
        if (key.isNotEmpty()){
            preferencesStorage.setLongValue(key, newValue)
        }
        onValueChanged?.invoke(newValue)
    }
}

@Composable
@JvmName("EditTextPreferenceItemFloat")
private fun EditTextPreferenceItem(
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
private fun EditTextPreferenceItem(
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
