package com.mobilerpgpack.phone.ui.items

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mobilerpgpack.phone.R

@Composable
fun ShowErrorDialog (message : String,showDialog : Boolean, dismissAction : () -> Unit){
    if (showDialog) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            textContentColor = MaterialTheme.colorScheme.onBackground,
            iconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            title = { Text(stringResource(R.string.error)) },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { dismissAction.invoke() })
                { Text(stringResource(R.string.ok_text)) }
            }, onDismissRequest = { dismissAction.invoke() })
    }
}