package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
    onValueChanged: ((String) -> Unit)? = null) {
    val cancelString = stringResource(R.string.cancel_text)
    val positiveString = stringResource(R.string.ok_text)
    val preferences = LocalPreferenceHandler.current

    TextPreference(
        title = { Text(title) },
        value = value,
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
    onValueChanged: ((String) -> Unit)? = null){
    val initialValue by valueFlow.collectAsState(initial = "")
    EditTextPreferenceItem(title, initialValue, key, hint, onValueChanged)
}