package com.mobilerpgpack.phone.ui.items

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.github.sproctor.composepreferences.LocalPreferenceHandler
import com.github.sproctor.composepreferences.TextPreference
import com.mobilerpgpack.phone.R

@Composable
fun EditTextPreferenceItem(
    title: String,
    value: String,
    key : String = "",
    hint: String = "",
    onValueChanged: (String) -> Unit = {}) {
    val context = LocalContext.current
    val cancelString = context.getString(R.string.cancel_text)
    val positiveString = context.getString(R.string.ok_text)
    val preferences = LocalPreferenceHandler.current

    TextPreference(
        title = { Text(title) },
        value = value,
        onValueChanged = { newValue : String ->
            if (key.isNotEmpty()) {
                preferences.putString(key, newValue)
            }
            onValueChanged(newValue)
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