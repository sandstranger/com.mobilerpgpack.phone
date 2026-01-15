package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import com.mobilerpgpack.phone.R
import kotlinx.coroutines.flow.Flow

@Composable
fun DrawCommandLinePreferences(commandLineArgs : LiveData<String>, prefsKey : String){
    EditTextPreferenceItem(
        stringResource(R.string.command_line_args),
        value = commandLineArgs, prefsKey
    )
}