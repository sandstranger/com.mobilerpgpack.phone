package com.mobilerpgpack.phone.ui.items

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.Preferences
import com.codekidlabs.storagechooser.StorageChooser
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

@Composable
fun RequestPath(explorerItemTitle: String,
                previousSavedPath: String = "",
                key : Preferences.Key<String>? = null,
                requestMode: RequestPathMode = RequestPathMode.Directory,
                onPathSelected: ((String) -> Unit)? = null) {
    val activity = LocalActivity.current
    var currentPath by rememberSaveable(previousSavedPath)
    {
        mutableStateOf(previousSavedPath)
    }

    PreferenceItem(
        explorerItemTitle, currentPath,
        onClick = {
            val fileChooser : StorageChooser = get (StorageChooser::class.java, parameters =
                { parametersOf(requestMode, activity) } )
            val prefsStorage : PreferencesStorage = get(PreferencesStorage::class.java)

            fileChooser.setOnSelectListener { path ->
                onPathSelected?.invoke(path)
                if (path.isNotEmpty() && key!=null){
                    prefsStorage.setStringValue(key, path)
                }
            }
            fileChooser.show()
        })
}

@Composable
fun RequestPath(explorerItemTitle: String,
                previousSavedPathFlow: Flow<String> = emptyFlow(),
                key : Preferences.Key<String>? = null,
                requestMode: RequestPathMode = RequestPathMode.Directory,
                onPathSelected: ((String) -> Unit)? = null) {
    val flowValue by previousSavedPathFlow.collectAsState(initial = "")
    RequestPath(explorerItemTitle, flowValue, key, requestMode, onPathSelected)
}

enum class RequestPathMode{
    Directory,
    Archive,
    Cue
}