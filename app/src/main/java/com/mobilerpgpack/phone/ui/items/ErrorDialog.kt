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
fun ShowErrorDialog (message : String,showDialog : Boolean, dismissAction : () -> Unit){
    val onSurfaceVariantColor = getOnSurfaceVariantColor()
    val onSurfaceColor = getOnSurfaceColor()
    val primaryColor = getPrimaryColor()
    val surfaceContainerHighColor = getSurfaceContainerHighColor()

    if (showDialog) {
        AlertDialog(
            containerColor = surfaceContainerHighColor,
            textContentColor = onSurfaceVariantColor,
            iconContentColor = onSurfaceVariantColor,
            titleContentColor = onSurfaceColor,
            title = { Text(stringResource(R.string.error), color = onSurfaceColor) },
            text = { Text(message, color = onSurfaceVariantColor) },
            confirmButton = {
                TextButton(onClick = { dismissAction.invoke() }, colors = getTextButtonsColors())
                { Text(stringResource(R.string.ok_text), color = primaryColor) }
            }, onDismissRequest = { dismissAction.invoke() })
    }
}