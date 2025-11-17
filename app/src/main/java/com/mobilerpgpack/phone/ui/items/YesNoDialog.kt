package com.mobilerpgpack.phone.ui.items

import android.icu.text.CaseMap
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mobilerpgpack.phone.R

@Composable
fun ShowYesNoDialog (title: String = "", message : String,
                     positiveAction : (() -> Unit)? = null,
                     negativeAction :(() -> Unit)? = null ){
    AlertDialog(
        onDismissRequest = { negativeAction?.invoke() },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = {
                positiveAction?.invoke()
            }) { Text(stringResource(R.string.yes_text)) }
        },
        dismissButton = {
            TextButton(onClick = { negativeAction?.invoke() }) { Text(stringResource(R.string.no_text)) }
        })
}