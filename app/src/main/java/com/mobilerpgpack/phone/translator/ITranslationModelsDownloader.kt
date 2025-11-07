package com.mobilerpgpack.phone.translator

interface ITranslationModelsDownloader {

    suspend fun downloadModelIfNeeded(onProgress: (String) -> Unit = { })

    fun cancelDownloadModel()
}