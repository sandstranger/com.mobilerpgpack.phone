package com.mobilerpgpack.phone.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mobilerpgpack.phone.R

@Composable
fun LoadingModelDialogWithCancel(
    show: Boolean,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    if (show) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(context.getString(R.string.loading_model_title)) },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(context.getString(R.string.wait_text))
                }
            },
            confirmButton = {
                TextButton(onClick = onCancel) {
                    Text(context.getString(R.string.cancel_text))
                }
            }
        )
    }
}