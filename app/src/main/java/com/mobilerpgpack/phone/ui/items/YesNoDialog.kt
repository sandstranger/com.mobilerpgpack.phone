package com.mobilerpgpack.phone.ui.items

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mobilerpgpack.phone.R

@Composable
fun ShowYesNoDialog (title: String = "", message : String,
                     positiveAction : (() -> Unit)? = null,
                     negativeAction :(() -> Unit)? = null ){
    AlertDialog(title = { Text(title) },
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = MaterialTheme.colorScheme.onBackground,
        iconContentColor = MaterialTheme.colorScheme.onBackground,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = {
                positiveAction?.invoke()
            }) { Text(stringResource(R.string.yes_text)) }
        },
        onDismissRequest = { negativeAction?.invoke() },
        dismissButton = {
            TextButton(onClick = { negativeAction?.invoke() }) { Text(stringResource(R.string.no_text)) }
        })
}