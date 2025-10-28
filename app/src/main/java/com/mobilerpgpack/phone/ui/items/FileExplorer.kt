package com.mobilerpgpack.phone.ui.items

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.codekidlabs.storagechooser.StorageChooser
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun RequestPath(explorerItemTitle: String,
                onPathSelected: (String) -> Unit,
                previousSavedPath: String = "",
                requestOnlyDirectory: Boolean = false) {
    val activity = LocalActivity.current
    var currentPath by rememberSaveable(previousSavedPath)
    {
        mutableStateOf(previousSavedPath)
    }

    val fileChooser = koinInject<StorageChooser>(parameters = {
        parametersOf(requestOnlyDirectory,activity ) })

    PreferenceItem(
        explorerItemTitle, currentPath,
        onClick = {
            fileChooser.setOnSelectListener { path -> onPathSelected(path) }
            fileChooser.show()
        })
}