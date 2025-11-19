package com.mobilerpgpack.phone.translator

interface ITranslationModelsDownloader {

    var allowDownloadingOveMobile: Boolean
    
    suspend fun downloadModelIfNeeded(onProgress: (String) -> Unit = { })

    fun cancelDownloadModel()
}