package com.mobilerpgpack.phone.ui.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.mobilerpgpack.phone.utils.requestDirectory
import com.mobilerpgpack.phone.utils.requestResourceFile

@Composable
fun RequestPath(explorerItemTitle: String,
                onPathSelected: (String) -> Unit,
                previousSavedPath: String = "",
                requestOnlyDirectory: Boolean = false) {
    val context = LocalContext.current
    var currentPath by rememberSaveable(previousSavedPath)
    {
        mutableStateOf(previousSavedPath)
    }

    PreferenceItem(
        explorerItemTitle, currentPath,
        onClick = {
            if (requestOnlyDirectory) {
                context.requestDirectory(
                    onDirectorySelected = { selectedPath -> onPathSelected(selectedPath) })
            } else {
                context.requestResourceFile(onFileSelected =
                    { selectedPath -> onPathSelected(selectedPath) })
            }
        })
}