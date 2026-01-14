package com.mobilerpgpack.phone.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.getEditTextFieldColors
import com.mobilerpgpack.phone.ui.getOnBackgroundColor
import com.mobilerpgpack.phone.ui.getOnSurfaceColor
import com.mobilerpgpack.phone.ui.getOnSurfaceVariantColor
import com.mobilerpgpack.phone.ui.getPrimaryColor
import com.mobilerpgpack.phone.ui.getSurfaceContainerHighColor
import com.mobilerpgpack.phone.ui.getTextButtonsColors
import com.mobilerpgpack.phone.utils.getComposableValue

@Composable
fun EditTextItem(
    title: String,
    value: String,
    hint: String = "",
    singleLine : Boolean = true,
    keyboardType : KeyboardType = KeyboardType.Text,
    onValueChange: ((String) -> Unit)? = null
) {
    val hint = rememberSaveable(hint) { hint }
    val keyboardType = remember { keyboardType }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var currentTextValue by rememberSaveable (value) { mutableStateOf(value) }
    val onSurfaceVariantColor = getOnSurfaceVariantColor()
    val onSurfaceColor = getOnSurfaceColor()
    val primaryColor = getPrimaryColor()
    val surfaceContainerHighColor = getSurfaceContainerHighColor()
    val textButtonsColor = getTextButtonsColors()
    val onBackgroundColor = getOnBackgroundColor()
    val hintColor = remember {Color.Gray }
    val textColor = if (currentTextValue.isEmpty() && hint.isNotEmpty()) hintColor else onBackgroundColor
    var textToShowWhenDialogBoxActive by rememberSaveable { mutableStateOf(currentTextValue) }
    val showText = (if (showDialog) textToShowWhenDialogBoxActive.isNotEmpty() else
        currentTextValue.isNotEmpty()) || hint.isNotEmpty()

    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { showDialog = true }
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)) {

        Text(text = title, color = onBackgroundColor)

        if (showText) {
            Text(
                text = if (showDialog) textToShowWhenDialogBoxActive.ifEmpty { hint }
                else currentTextValue.ifEmpty { hint },
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
            )
        }
    }

    if (showDialog) {
        val savedTextValue by rememberSaveable { mutableStateOf(currentTextValue)}
        textToShowWhenDialogBoxActive = savedTextValue
        AlertDialog(
            containerColor = surfaceContainerHighColor,
            textContentColor = onSurfaceVariantColor,
            iconContentColor = onSurfaceVariantColor,
            titleContentColor = onSurfaceColor,
            onDismissRequest = {
                currentTextValue = savedTextValue
                showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onValueChange?.invoke(currentTextValue)
                    showDialog = false
                }, colors = textButtonsColor) {
                    Text(stringResource(R.string.ok_text), color = primaryColor)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    currentTextValue = savedTextValue
                    showDialog = false }, colors = textButtonsColor) {
                    Text(stringResource(R.string.cancel_text), color = primaryColor)
                }
            },
            title = { Text(text = title, color = onSurfaceColor) },
            text = {
                OutlinedTextField(
                    value = currentTextValue,
                    onValueChange = { currentTextValue = it },
                    placeholder = { if (currentTextValue.isEmpty() && hint.isNotEmpty()) {
                        Text(hint, color = hintColor)
                    } },
                    singleLine = singleLine,
                    colors = getEditTextFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
                )
            }
        )
    }
}

@Composable
fun EditTextItem(
    title: String,
    value: LiveData<String>,
    hint: String = "",
    singleLine : Boolean = true,
    keyboardType : KeyboardType = KeyboardType.Text,
    onValueChange: ((String) -> Unit)? = null
){
    EditTextItem(title, value.getComposableValue(), hint, singleLine, keyboardType, onValueChange)
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
@JvmName("EditTextItemAsLiveDataInt")
fun EditTextItem(
    title: String,
    value: LiveData<Int>,
    hint: String = "",
    singleLine : Boolean = true,
    onValueChange: ((Int) -> Unit)? = null){
    EditTextItem(title, value.getComposableValue(), hint, singleLine, onValueChange)
}

@Composable
fun EditTextItem(
    title: String,
    value: Float,
    hint: String = "",
    singleLine : Boolean = true,
    onValueChange: ((Float) -> Unit)? = null){
    EditTextItem(title, value.toString(), hint, singleLine, keyboardType = KeyboardType.Decimal){
        val newValue = it.toFloatOrNull() ?: 0.0f
        onValueChange?.invoke(newValue)
    }
}

@Composable
@JvmName("EditTextItemAsLiveDataFloat")
fun EditTextItem(
    title: String,
    value: LiveData<Float>,
    hint: String = "",
    singleLine : Boolean = true,
    onValueChange: ((Float) -> Unit)? = null){
    EditTextItem(title, value.getComposableValue(), hint, singleLine, onValueChange)
}