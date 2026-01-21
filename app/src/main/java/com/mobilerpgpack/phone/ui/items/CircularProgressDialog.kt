package com.mobilerpgpack.phone.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.ui.getOnSurfaceColor
import com.mobilerpgpack.phone.ui.getOnSurfaceVariantColor
import com.mobilerpgpack.phone.ui.getPrimaryColor
import com.mobilerpgpack.phone.ui.getSurfaceContainerHighColor

@Composable
fun CircularProgressDialog(title : String = "",
                           textToShow : String) {
    val onSurfaceVariantColor = getOnSurfaceVariantColor()
    val onSurfaceColor = getOnSurfaceColor()
    val surfaceContainerHighColor = getSurfaceContainerHighColor()
    val primaryColor = getPrimaryColor()

    AlertDialog(
        containerColor = surfaceContainerHighColor,
        textContentColor = onSurfaceVariantColor,
        iconContentColor = onSurfaceVariantColor,
        titleContentColor = onSurfaceColor,
        onDismissRequest = { },
        title = { Text(title, color = onSurfaceColor) },
        text = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = primaryColor)
                Text(textToShow, color = onSurfaceVariantColor)
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}