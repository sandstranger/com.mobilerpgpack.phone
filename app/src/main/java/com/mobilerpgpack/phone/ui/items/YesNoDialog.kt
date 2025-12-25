package com.mobilerpgpack.phone.ui.items

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.getOnSurfaceColor
import com.mobilerpgpack.phone.ui.getOnSurfaceVariantColor
import com.mobilerpgpack.phone.ui.getPrimaryColor
import com.mobilerpgpack.phone.ui.getSurfaceContainerHighColor
import com.mobilerpgpack.phone.ui.getTextButtonsColors

@Composable
fun ShowYesNoDialog (title: String = "", message : String,
                     positiveAction : (() -> Unit)? = null,
                     negativeAction :(() -> Unit)? = null ){
    val onSurfaceVariantColor = getOnSurfaceVariantColor()
    val onSurfaceColor = getOnSurfaceColor()
    val primaryColor = getPrimaryColor()
    val surfaceContainerHighColor = getSurfaceContainerHighColor()
    val buttonsColors = getTextButtonsColors()

    AlertDialog(title = { Text(title, color = onSurfaceColor) },
        containerColor = surfaceContainerHighColor,
        textContentColor = onSurfaceVariantColor,
        iconContentColor = onSurfaceVariantColor,
        titleContentColor = onSurfaceColor,
        text = { Text(message, color = onSurfaceVariantColor) },
        confirmButton = {
            TextButton(onClick = {
                positiveAction?.invoke()
            }, colors = buttonsColors) { Text(stringResource(R.string.yes_text), color = primaryColor) }
        },
        onDismissRequest = { negativeAction?.invoke() },
        dismissButton = {
            TextButton(onClick = { negativeAction?.invoke() }, colors = buttonsColors) { Text(stringResource(R.string.no_text),
                color = primaryColor) }
        })
}