package com.mobilerpgpack.phone.ui.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.mobilerpgpack.phone.R
import kotlinx.coroutines.flow.Flow

@Composable
fun DrawCommandLinePreferences(commandLineArgsFlow : Flow<String>, prefsKey : String){
    val context = LocalContext.current
    val commandLineArgs by commandLineArgsFlow.collectAsState(initial = "")

    EditTextPreferenceItem(context.getString(R.string.command_line_args),
        value = commandLineArgs, prefsKey)
}