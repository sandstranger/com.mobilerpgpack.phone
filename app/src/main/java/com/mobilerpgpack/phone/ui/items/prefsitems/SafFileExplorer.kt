package com.mobilerpgpack.phone.ui.items.prefsitems

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import com.mobilerpgpack.phone.ui.items.ShowErrorDialog
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.sharesprefs.Key
import com.opentouchgaming.saffal.UtilsSAF.addTreeRootFromUri
import com.opentouchgaming.saffal.UtilsSAF.getTreeRoots
import com.opentouchgaming.saffal.UtilsSAF.saveTreeRoots
import org.koin.compose.koinInject
import java.io.File
import java.util.Locale

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
internal fun RequestSafPath(
    explorerItemTitle: String,
    previousSavedPath: String = "",
    key: Key<String>? = null,
    requestMode: RequestPathMode = RequestPathMode.Directory,
    requiredFileExtensions: Collection<String> = emptyList(),
    predefinedPath: String = "",
    onPathSelected: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val prefsStorage: PreferencesStorage = koinInject()

    var showErrorDialogBox by rememberSaveable { mutableStateOf(false) }
    var errorMessageToShow by rememberSaveable { mutableStateOf("") }

    val initialUri = remember(predefinedPath) {
        if (predefinedPath.isNotEmpty()) {
            try {
                predefinedPath.toUri()
            } catch (_: Exception) {
                null
            }
        } else null
    }

    val mimeTypes = remember(requiredFileExtensions) {
        extensionsToMimeTypes(requiredFileExtensions)
    }

    val directoryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        if (uri != null) {
            val flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, flags)
            onUriSelected(uri, key, prefsStorage, context, onPathSelected)
        }
    }

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            onUriSelected(uri, key, prefsStorage, context, onPathSelected)
        }
    }

    PreferenceItem(
        title = explorerItemTitle,
        initialValue = previousSavedPath,
        onClick = {
            when (requestMode) {
                RequestPathMode.Directory -> {
                    directoryLauncher.launch(initialUri)
                }

                RequestPathMode.File -> {
                    val intent = fileLauncher.contract.createIntent(context, mimeTypes)
                    if (initialUri != null) {
                        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri)
                    }
                    fileLauncher.launch(mimeTypes)
                }
            }
        }
    )

    ShowErrorDialog(errorMessageToShow, showErrorDialogBox) {
        showErrorDialogBox = false
        errorMessageToShow = ""
    }
}

@Composable
@JvmName("RequestSafPathLiveData")
internal fun RequestSafPath(
    explorerItemTitle: String,
    previousSavedPath: LiveData<String?>?,
    key: Key<String>? = null,
    requestMode: RequestPathMode = RequestPathMode.Directory,
    requiredFileExtensions: Collection<String> = emptyList(),
    predefinedPath: String = "",
    onPathSelected: ((String) -> Unit)? = null
) {
    val liveState = previousSavedPath?.observeAsState("")
    var savedValue by rememberSaveable(liveState?.value) { mutableStateOf(liveState?.value ?: "") }
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
@JvmName("RequestSafPathLiveDataNullable")
internal fun RequestSafPath(
    explorerItemTitle: String,
    previousSavedPath: LiveData<String>?,
    key: Key<String>? = null,
    requestMode: RequestPathMode = RequestPathMode.Directory,
    requiredFileExtensions: Collection<String> = emptyList(),
    predefinedPath: String = "",
    onPathSelected: ((String) -> Unit)? = null
) {
    val liveState = previousSavedPath?.observeAsState("")
    var savedValue by rememberSaveable(liveState?.value) { mutableStateOf(liveState?.value ?: "") }
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

private fun getFileNameFromUri(context: Context, uri: Uri): String? {
    var name: String? = null
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (index >= 0) name = cursor.getString(index)
        }
    }
    return name
}

private fun onUriSelected(
    uri: Uri,
    key: Key<String>?,
    prefsStorage: PreferencesStorage,
    context: Context,
    onPathSelected: ((String) -> Unit)?
) {
    val realFilePath = if (DocumentsContract.isTreeUri(uri)) {
        addTreeRootFromUri(uri)
        saveTreeRoots()
        getTreeRoots().last().rootPath
    } else {
        val fileName = getFileNameFromUri(context, uri) ?: "selected_file"
        try {
            val destFile = File(context.getExternalFilesDir(null)!!, fileName)
            destFile.parentFile?.mkdirs()
            if (!destFile.exists()) {
                destFile.createNewFile()
            }
            context.contentResolver.openInputStream(uri)?.use { input ->
                destFile.outputStream().use { output -> input.copyTo(output) }
            }
            destFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    if (!realFilePath.isNullOrEmpty()) {
        if (key != null) {
            prefsStorage.setStringValue(key, realFilePath)
        }
        onPathSelected?.invoke(realFilePath)
    }
}

private fun extensionsToMimeTypes(extensions: Collection<String>): Array<String> {
    if (extensions.isEmpty()) return arrayOf("*/*")

    val mimeSet = mutableSetOf<String>()
    val mimeMap = MimeTypeMap.getSingleton()
    val manualMimeMap = mapOf(
        "zip" to "application/zip",
        "rar" to "application/x-rar-compressed",
        "7z" to "application/x-7z-compressed",
        "ipa" to "application/octet-stream",
        "tar" to "application/x-tar",
        "gz" to "application/gzip",
        "bz2" to "application/x-bzip2",
        "xz" to "application/x-xz",
        "pdf" to "application/pdf",
        "doc" to "application/msword",
        "docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "txt" to "text/plain",
        "json" to "application/json",
        "png" to "image/png",
        "jpg" to "image/jpeg",
        "jpeg" to "image/jpeg",
        "gif" to "image/gif",
        "z64" to "application/zip"
    )

    for (ext in extensions) {
        val cleanExt = ext.trimStart('.').lowercase(Locale.ROOT)
        val systemMime = mimeMap.getMimeTypeFromExtension(cleanExt)
        if (systemMime != null) {
            mimeSet.add(systemMime)
            continue
        }
        val manualMime = manualMimeMap[cleanExt]
        if (manualMime != null) {
            mimeSet.add(manualMime)
            continue
        }
        return arrayOf("*/*")
    }

    return if (mimeSet.isEmpty()) arrayOf("*/*") else mimeSet.toTypedArray()
}