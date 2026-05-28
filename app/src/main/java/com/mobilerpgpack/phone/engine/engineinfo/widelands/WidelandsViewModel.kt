package com.mobilerpgpack.phone.engine.engineinfo.widelands

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.engine.engineinfo.widelands.WidelandsEngineInfo.Companion.WIDELANDS_FILES_CONTENT_CURRENT_VERSION
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.main.RESOURCES_DOWNLOADER_ID
import com.mobilerpgpack.phone.net.IDriveDownloader
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.isWifiConnected
import com.mobilerpgpack.phone.utils.unzipArchive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File

class WidelandsViewModel : ViewModel(), KoinComponent {
    private val context : Context by inject()
    private val scope : CoroutineScope by inject (
        named(KoinModulesProvider.BACKGROUND_THREAD_COROUTINE_KEY))
    private val engineInfo : WidelandsEngineInfo by inject()
    private val prefsStorage : PreferencesStorage by inject()
    private val downloader : IDriveDownloader by inject { parametersOf(RESOURCES_DOWNLOADER_ID) }
    private val zipFile : File by inject { parametersOf("widelands-data-files.zip") }
    private val allowDownloading get() = prefsStorage.allowWidelandsDownloadsOverMobile.value!! || context.isWifiConnected()
    private var downloadJob: Job? = null
    val downloadProgress = MutableLiveData("")
    val isLoading = MutableLiveData(false)

    init {
        if (WIDELANDS_FILES_CONTENT_CURRENT_VERSION != prefsStorage.widelandsFilesContentVersion.value!! ||
            !engineInfo.widelandsDataFolder.exists()){
            prefsStorage.setBooleanValue(prefsStorage.widelandsFilesContentDownloadedPrefsKey, false)
        }
    }

    fun startDownload() {
        if (isLoading.value!! || !allowDownloading) {
            return
        }
        isLoading.value = true
        if (downloadJob == null || downloadJob!!.isCompleted || downloadJob!!.isCancelled) {
            downloadJob = scope.launch {
                try {
                    downloadProgress.postValue("")
                    downloader.download(ZIP_FILE_ID, zipFile){
                        downloadProgress.postValue(it)
                    }
                    if(unzipArchive(zipFile, engineInfo.widelandsRootFolder.absolutePath,
                        ZIP_FILE_SHA256)) {
                        withContext(Dispatchers.Main) {
                            prefsStorage.setIntValue(
                                prefsStorage.widelandsFilesContentVersionPrefsKey,
                                WIDELANDS_FILES_CONTENT_CURRENT_VERSION
                            )
                            prefsStorage.setBooleanValue(
                                prefsStorage.widelandsFilesContentDownloadedPrefsKey,
                                true
                            )
                        }
                    }
                }
                finally {
                    isLoading.postValue(false)
                    downloadJob = null
                }
            }
        }
    }

    fun cancelDownload() {
        isLoading.value = false
        downloadJob?.cancel()
        downloadJob = null
    }

    private companion object {
        const val ZIP_FILE_ID = "123gI5pdyShoj2nX_lqYm-vlStRCXr4Z4"
        const val ZIP_FILE_SHA256 = "98dda156bd6c023149f38f9bfb023567cef1b3aaa6ac2d35b90bda58eb563036"
    }
}