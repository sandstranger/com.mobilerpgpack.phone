package com.mobilerpgpack.phone.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.R

@Composable
fun EditTextItem(
    title: String,
    value: String,
    hint: String = "",
    singleLine : Boolean = true,
    keyboardType : KeyboardType = KeyboardType.Text,
    onValueChange: ((String) -> Unit)? = null
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var tempValue by rememberSaveable(value) { mutableStateOf(value) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { showDialog = true }
        .padding(16.dp)) {

        Text(text = title)

        Text(
            text = tempValue.ifEmpty { hint },
            style = MaterialTheme.typography.bodyMedium,
            color = if (value.isNotEmpty()) Color.Unspecified else Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }

    if (showDialog) {

        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onValueChange?.invoke(tempValue)
                    showDialog = false
                }) {
                    Text(stringResource(R.string.ok_text))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel_text))
                }
            },
            title = { Text(text = title) },
            text = {
                OutlinedTextField(
                    value = tempValue,
                    onValueChange = { tempValue = it },
                    placeholder = { Text(hint) },
                    singleLine = singleLine,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
                )
            }
        )
    }
}

@Composable
fun <T> EditTextItem(
    title: String,
    value: T,
    hint: String = "",
    singleLine : Boolean = true,
    keyboardType : KeyboardType = KeyboardType.Text,
    onValueChange: ((String) -> Unit)? = null){
    EditTextItem(title, value.toString(), hint, singleLine, keyboardType, onValueChange)
}

@Composable
fun EditTextItem(
    title: String,
    value: Int,
    hint: String = "",
    singleLine : Boolean = true,
    onValueChange: ((Int) -> Unit)? = null){
    EditTextItem(title, value.toString(), hint, singleLine, keyboardType = KeyboardType.Number){
        val newValue = it.toIntOrNull() ?: 0
        onValueChange?.invoke(newValue)
    }
}

@Composable
fun EditTextItem(
    title: String,
    value: Float,
    hint: String = "",
    singleLine : Boolean = true,
    onValueChange: ((Float) -> Unit)? = null){
    EditTextItem(title, value.toString(), hint, singleLine, keyboardType = KeyboardType.Number){
        val newValue = it.toFloatOrNull() ?: 0.0f
        onValueChange?.invoke(newValue)
    }
}