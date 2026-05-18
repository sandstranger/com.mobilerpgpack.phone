package com.mobilerpgpack.phone.ui.screen.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.translator.ITranslationModelsDownloader
import com.mobilerpgpack.phone.utils.IAssetExtractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class DownloadViewModel : ViewModel(), KoinComponent {
    private val scope : CoroutineScope by inject (
        named(KoinModulesProvider.BACKGROUND_THREAD_COROUTINE_KEY))
    private val assetsExtractor : IAssetExtractor by inject ()
    private val translationModelsDownloader : ITranslationModelsDownloader = get()
    private var currentTranslationModelType : String? = null
    private var downloadJob: Job? = null
    val isLoading = MutableLiveData(false)
    val downloadProgress = MutableLiveData("")

    init {
        assetsExtractor.assetsStartedCopyListeners += { cancelDownload() }
    }

    fun onTranslationTypeChanged(translationModelType : String){
        if (currentTranslationModelType != translationModelType){
            currentTranslationModelType = translationModelType
            cancelDownload()
        }
    }

    fun startDownload() {
        if (isLoading.value!!) return

        isLoading.value = true

        if (downloadJob == null || downloadJob!!.isCompleted || downloadJob!!.isCancelled) {
            downloadJob = scope.launch {
                try {
                    downloadProgress.postValue("")
                    translationModelsDownloader.downloadModelIfNeeded { newValue ->
                        downloadProgress.postValue(newValue)
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
        translationModelsDownloader.cancelDownloadModel()
    }
}