package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.codekidlabs.storagechooser.StorageChooser
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.items.ShowErrorDialog
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.Key
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

@Composable
fun RequestPath(explorerItemTitle: String,
                previousSavedPath: String = "",
                key : Key<String>? = null,
                requestMode: RequestPathMode = RequestPathMode.Directory,
                requiredFileExtensions : Collection<String> = emptyList(),
                onPathSelected: ((String) -> Unit)? = null) {
    val activity = LocalActivity.current!!
    var showErrorDialogBox by rememberSaveable(false) { mutableStateOf(false) }
    var errorMessageToShow by rememberSaveable("") { mutableStateOf("")}
    var currentPath by rememberSaveable(previousSavedPath)
    {
        mutableStateOf(previousSavedPath)
    }

    PreferenceItem(
        explorerItemTitle, currentPath,
        onClick = {
            val fileChooser: StorageChooser = get(
                StorageChooser::class.java, parameters =
                    { parametersOf(requestMode, activity) })
            val prefsStorage: PreferencesStorage = get(PreferencesStorage::class.java)

            fileChooser.setOnSelectListener { path ->
                if (path.isNotEmpty()) {
                    if (requestMode == RequestPathMode.File &&
                        requiredFileExtensions.isNotEmpty() && requiredFileExtensions.all { !path.endsWith(it) }){
                        errorMessageToShow = activity.getString(R.string.file_extension_not_correct_error,
                            requiredFileExtensions.joinToString(" "))
                        showErrorDialogBox = true
                        return@setOnSelectListener
                    }
                    onPathSelected?.invoke(path)
                    if ( key != null) {
                        prefsStorage.setStringValue(key, path)
                    }
                }
            }
            fileChooser.show()
        })
    ShowErrorDialog(errorMessageToShow, showErrorDialogBox){
        showErrorDialogBox = false
        errorMessageToShow = ""
    }
}

enum class RequestPathMode{
    Directory,
    File
}