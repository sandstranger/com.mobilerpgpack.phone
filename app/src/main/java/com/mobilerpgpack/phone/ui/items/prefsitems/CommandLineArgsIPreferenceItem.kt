package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.mobilerpgpack.phone.R
import kotlinx.coroutines.flow.Flow

@Composable
fun DrawCommandLinePreferences(commandLineArgsFlow : Flow<String>, prefsKey : String){
    val commandLineArgs by commandLineArgsFlow.collectAsState(initial = "")

    EditTextPreferenceItem(
        stringResource(R.string.command_line_args),
        value = commandLineArgs, prefsKey
    )
}