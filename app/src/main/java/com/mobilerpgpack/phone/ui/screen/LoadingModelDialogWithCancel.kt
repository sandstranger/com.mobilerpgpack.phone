package com.mobilerpgpack.phone.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.getOnBackgroundColor
import com.mobilerpgpack.phone.ui.getOnSurfaceColor
import com.mobilerpgpack.phone.ui.getOnSurfaceVariantColor
import com.mobilerpgpack.phone.ui.getPrimaryColor
import com.mobilerpgpack.phone.ui.getSurfaceContainerHighColor
import com.mobilerpgpack.phone.ui.getTextButtonsColors

@Composable
fun LoadingModelDialogWithCancel(
    show: Boolean,
    progress: String = "",
    onClose: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val onSurfaceVariantColor = getOnSurfaceVariantColor()
    val onSurfaceColor = getOnSurfaceColor()
    val surfaceContainerHighColor = getSurfaceContainerHighColor()
    val buttonColor = getTextButtonsColors()
    val primaryColor = getPrimaryColor()

    if (show) {
        AlertDialog(
            containerColor = surfaceContainerHighColor,
            textContentColor = onSurfaceVariantColor,
            iconContentColor = onSurfaceVariantColor,
            titleContentColor = onSurfaceColor,
            onDismissRequest = { },
            title = { Text(stringResource(R.string.loading_model_title), color = onSurfaceColor) },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = primaryColor)
                    Text(stringResource(R.string.wait_text) + "\n$progress", color = onSurfaceVariantColor)
                }
            },
            confirmButton = {
                TextButton(onClick = onClose, colors = buttonColor) {
                    Text(stringResource(R.string.close_text), color = primaryColor)
                }
            },
            dismissButton = {
                TextButton(onClick = onCancel, colors = buttonColor) {
                    Text(stringResource(R.string.cancel_download), color = primaryColor)
                }
            }
        )
    }
}