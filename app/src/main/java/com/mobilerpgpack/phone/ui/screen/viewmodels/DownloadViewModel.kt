package com.mobilerpgpack.phone.ui.screen.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.translator.ITranslationManager
import com.mobilerpgpack.phone.translator.TranslationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DownloadViewModel() : ViewModel(), KoinComponent {

    private val scope : CoroutineScope = get()

    private val translationManager : ITranslationManager = get()

    var isLoading by mutableStateOf(false)

    var downloadProgress by mutableStateOf("")
        private set

    private var currentTranslationModelType : String? = null

    private var downloadJob: Job? = null

    fun onTranslationTypeChanged(translationModelType : String){
        if (currentTranslationModelType != translationModelType){
            currentTranslationModelType = translationModelType
            cancelDownload()
        }
    }

    fun startDownload() {
        if (isLoading) return

        isLoading = true

        if (downloadJob == null || downloadJob!!.isCompleted || downloadJob!!.isCancelled) {
            downloadJob = scope.launch {
                try {
                    downloadProgress = ""
                    translationManager.downloadModelIfNeeded { newValue ->
                        downloadProgress = newValue
                    }
                }
                catch (e : Exception){
                }
                finally {
                    isLoading = false
                    downloadJob = null
                }
            }
        }
    }

    fun cancelDownload() {
        isLoading = false
        downloadJob?.cancel()
        downloadJob = null
        translationManager.cancelDownloadModel()
    }
}