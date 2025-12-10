package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.sproctor.composepreferences.LocalPreferenceHandler
import com.github.sproctor.composepreferences.TextPreference
import com.mobilerpgpack.phone.R
import kotlinx.coroutines.flow.Flow

@Composable
fun EditTextPreferenceItem(
    title: String,
    value: String,
    key : String = "",
    hint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: ((String) -> Unit)? = null) {
    val cancelString = stringResource(R.string.cancel_text)
    val positiveString = stringResource(R.string.ok_text)
    val preferences = LocalPreferenceHandler.current

    TextPreference(
        title = { Text(title) },
        value = value,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        onValueChanged = { newValue : String ->
            if (key.isNotEmpty()) {
                preferences.putString(key, newValue)
            }
            onValueChanged?.invoke(newValue)
        },
        summary = {  Text(
            text = value.ifEmpty { hint },
            style = MaterialTheme.typography.bodyMedium,
            color = if (value.isNotEmpty()) Color.Unspecified else Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        ) },
        confirmText = positiveString,
        dismissText = cancelString )
}

@Composable
fun EditTextPreferenceItem(
    title: String,
    valueFlow: Flow<String>,
    key : String = "",
    hint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: ((String) -> Unit)? = null){
    val initialValue by valueFlow.collectAsState(initial = "")
    EditTextPreferenceItem(title, initialValue, key, hint,keyboardType, onValueChanged)
}

@Composable
@JvmName("EditTextPreferenceItemFloat")
fun EditTextPreferenceItem(
    title: String,
    valueFlow: Flow<Float>,
    key : String = "",
    hint: String = "",
    onValueChanged: ((Float) -> Unit)? = null){
    val preferences = LocalPreferenceHandler.current
    val initialValue by valueFlow.collectAsState(initial = 0f)
    EditTextPreferenceItem(title, initialValue.toString(), "", hint, KeyboardType.Decimal){
        val newValue = it.toFloatOrNull() ?: 0f
        if (key.isNotEmpty()){
            preferences.putFloat(key,newValue)
        }
        onValueChanged?.invoke(newValue)
    }
}

@Composable
@JvmName("EditTextPreferenceItemInt")
fun EditTextPreferenceItem(
    title: String,
    valueFlow: Flow<Int>,
    hint: String = "",
    onValueChanged: ((Int) -> Unit)? = null){
    val initialValue by valueFlow.collectAsState(initial = 0)
    EditTextPreferenceItem(title, initialValue.toString(), "", hint,KeyboardType.Number){
        onValueChanged?.invoke(it.toIntOrNull() ?: 0)
    }
}