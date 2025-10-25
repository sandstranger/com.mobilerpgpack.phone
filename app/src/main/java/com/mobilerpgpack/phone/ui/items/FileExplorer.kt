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

    val storageChooserBuilder = koinInject<StorageChooser.Builder>(parameters = {
        parametersOf(activity ) })

    PreferenceItem(
        explorerItemTitle, currentPath,
        onClick = {

            if (requestOnlyDirectory){
                storageChooserBuilder.setType(StorageChooser.DIRECTORY_CHOOSER)
            }
            else{
                storageChooserBuilder
                    .setType(StorageChooser.FILE_PICKER)
                    .filter(StorageChooser.FileType.ARCHIVE)
            }

            val chooser = storageChooserBuilder.build();
            chooser.setOnSelectListener { path -> onPathSelected(path) }
            chooser.show()
        })
}