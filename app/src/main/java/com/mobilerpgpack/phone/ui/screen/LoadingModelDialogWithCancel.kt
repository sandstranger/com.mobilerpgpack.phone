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

@Composable
fun LoadingModelDialogWithCancel(
    show: Boolean,
    progress: String = "",
    onClose: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    if (show) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            textContentColor = MaterialTheme.colorScheme.onBackground,
            iconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            onDismissRequest = { },
            title = { Text(stringResource(R.string.loading_model_title)) },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Text(stringResource(R.string.wait_text) + "\n$progress")
                }
            },
            confirmButton = {
                TextButton(onClick = onClose) {
                    Text(stringResource(R.string.close_text))
                }
            },
            dismissButton = {
                TextButton(onClick = onCancel) {
                    Text(stringResource(R.string.cancel_download))
                }
            }
        )
    }
}