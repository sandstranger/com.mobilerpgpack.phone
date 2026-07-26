package com.mobilerpgpack.phone.ui.items.prefsitems

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.codekidlabs.storagechooser.StorageChooser
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.items.ShowErrorDialog
import com.mobilerpgpack.phone.ui.items.viewmodel.FileExplorerViewModel
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.Key
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

@Composable
@JvmName("RequestLegacyPathLiveData")
fun RequestLegacyPath(
    explorerItemTitle: String,
    previousSavedPath: LiveData<String?>? = null,
    key: Key<String>? = null,
    requestMode: RequestPathMode = RequestPathMode.Directory,
    requiredFileExtensions: Collection<String> = emptyList(),
    predefinedPath: String = "",
    onPathSelected: ((String) -> Unit)? = null
) {
    val liveDataState = previousSavedPath?.observeAsState("")
    var savedValue by rememberSaveable(liveDataState?.value) {
        mutableStateOf(
            liveDataState?.value ?: ""
        )
    }
    RequestPath(
        explorerItemTitle,
        savedValue,
        key,
        requestMode,
        requiredFileExtensions,
        predefinedPath,
        onPathSelected
    )
}

@Composable
@JvmName("RequestLegacyPathLiveDataNullable")
fun RequestLegacyPath(
    explorerItemTitle: String,
    previousSavedPath: LiveData<String>? = null,
    key: Key<String>? = null,
    requestMode: RequestPathMode = RequestPathMode.Directory,
    requiredFileExtensions: Collection<String> = emptyList(),
    predefinedPath: String = "",
    onPathSelected: ((String) -> Unit)? = null
) {
    val liveDataState = previousSavedPath?.observeAsState("")
    var savedValue by rememberSaveable(liveDataState?.value) {
        mutableStateOf(
            liveDataState?.value ?: ""
        )
    }
    RequestPath(
        explorerItemTitle, savedValue, key, requestMode,
        requiredFileExtensions, predefinedPath, onPathSelected
    )
}

@Composable
fun RequestLegacyPath(
    explorerItemTitle: String,
    previousSavedPath: String = "",
    key: Key<String>? = null,
    requestMode: RequestPathMode = RequestPathMode.Directory,
    requiredFileExtensions: Collection<String> = emptyList(),
    predefinedPath: String = "",
    onPathSelected: ((String) -> Unit)? = null
) {
    val activity = LocalActivity.current!!
    var showErrorDialogBox by rememberSaveable { mutableStateOf(false) }
    var errorMessageToShow by rememberSaveable { mutableStateOf("") }
    val key = remember(key) { key }
    val predefinedPath by rememberSaveable(predefinedPath) {
        mutableStateOf(predefinedPath)
    }
    val prefsStorage: PreferencesStorage = koinInject()
    val fileExplorerViewModel: FileExplorerViewModel = koinViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            with(fileExplorerViewModel) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    storageChooser?.close()
                    storageChooser = null
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    PreferenceItem(
        explorerItemTitle, previousSavedPath,
        onClick = {
            with(
                get<StorageChooser>(
                    StorageChooser::class.java, parameters =
                        { parametersOf(requestMode, predefinedPath, activity) })
            ) {
                fileExplorerViewModel.storageChooser = this

                setOnSelectListener { path ->
                    fileExplorerViewModel.storageChooser = null
                    if (path.isNotEmpty()) {
                        if (requestMode == RequestPathMode.File &&
                            requiredFileExtensions.isNotEmpty() && requiredFileExtensions.all {
                                !path.endsWith(
                                    it
                                )
                            }
                        ) {
                            errorMessageToShow = activity.getString(
                                R.string.file_extension_not_correct_error,
                                requiredFileExtensions.joinToString(" ")
                            )
                            showErrorDialogBox = true
                            return@setOnSelectListener
                        }
                        if (key != null) {
                            prefsStorage.setStringValue(key, path)
                        }
                        onPathSelected?.invoke(path)
                    }
                }
                show()
            }
        })
    ShowErrorDialog(errorMessageToShow, showErrorDialogBox) {
        showErrorDialogBox = false
        errorMessageToShow = ""
    }
}
