package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.LiveData
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.Key
import org.koin.compose.koinInject

@Composable
@JvmName("RequestPathLiveData")
fun RequestPath(
    explorerItemTitle: String,
    previousSavedPath: LiveData<String?>? = null,
    key: Key<String>? = null,
    requestMode: RequestPathMode = RequestPathMode.Directory,
    requiredFileExtensions: Collection<String> = emptyList(),
    predefinedPath: String = "",
    onPathSelected: ((String) -> Unit)? = null
) {
    val enableSaf = EnableSafUsing()
    if (enableSaf) {
        RequestSafPath(
            explorerItemTitle, previousSavedPath, key, requestMode, requiredFileExtensions,
            predefinedPath, onPathSelected
        )
    } else {
        RequestLegacyPath(
            explorerItemTitle, previousSavedPath, key, requestMode, requiredFileExtensions,
            predefinedPath, onPathSelected
        )
    }
}

@Composable
@JvmName("RequestPathLiveDataNullable")
fun RequestPath(
    explorerItemTitle: String,
    previousSavedPath: LiveData<String>? = null,
    key: Key<String>? = null,
    requestMode: RequestPathMode = RequestPathMode.Directory,
    requiredFileExtensions: Collection<String> = emptyList(),
    predefinedPath: String = "",
    onPathSelected: ((String) -> Unit)? = null
) {
    val enableSaf = EnableSafUsing()
    if (enableSaf) {
        RequestSafPath(
            explorerItemTitle, previousSavedPath, key, requestMode, requiredFileExtensions,
            predefinedPath, onPathSelected
        )
    } else {
        RequestLegacyPath(
            explorerItemTitle, previousSavedPath, key, requestMode, requiredFileExtensions,
            predefinedPath, onPathSelected
        )
    }
}

@Composable
fun RequestPath(
    explorerItemTitle: String,
    previousSavedPath: String = "",
    key: Key<String>? = null,
    requestMode: RequestPathMode = RequestPathMode.Directory,
    requiredFileExtensions: Collection<String> = emptyList(),
    predefinedPath: String = "",
    onPathSelected: ((String) -> Unit)? = null
) {
    val enableSaf = EnableSafUsing()
    if (enableSaf) {
        RequestSafPath(
            explorerItemTitle, previousSavedPath, key, requestMode, requiredFileExtensions,
            predefinedPath, onPathSelected
        )
    } else {
        RequestLegacyPath(
            explorerItemTitle, previousSavedPath, key, requestMode, requiredFileExtensions,
            predefinedPath, onPathSelected
        )
    }
}

@Composable
private fun EnableSafUsing(): Boolean {
    val prefsStorage: PreferencesStorage = koinInject()
    val enableSafLiveDataState = prefsStorage.enableSAF.observeAsState(false)
    val enableSaf by rememberSaveable(enableSafLiveDataState.value) {
        mutableStateOf(enableSafLiveDataState.value)
    }
    return enableSaf
}

enum class RequestPathMode {
    Directory,
    File
}