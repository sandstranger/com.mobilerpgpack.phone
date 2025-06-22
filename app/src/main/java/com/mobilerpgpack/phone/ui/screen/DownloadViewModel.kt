package com.mobilerpgpack.phone.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.translator.TranslatorApp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DownloadViewModel(
) : ViewModel() {

    var isLoading by mutableStateOf(false)

    var downloadProgress by mutableStateOf("")   // будет хранить, например, "25%"
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
        if (isLoading) return  // уже идёт

        isLoading = true

        if (downloadJob!=null){
            return
        }

        downloadJob = TranslatorApp.globalScope.launch {
            try {
                downloadProgress = ""
                TranslationManager.downloadModelIfNeeded { newValue ->
                    downloadProgress = newValue
                }
            } finally {
                isLoading = false
                downloadJob = null
            }
        }
    }

    fun cancelDownload() {
        isLoading = false
        downloadJob?.cancel()
        downloadJob = null
        TranslationManager.cancelDownloadModel()
    }
}