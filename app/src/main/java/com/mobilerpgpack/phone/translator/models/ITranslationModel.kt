package com.mobilerpgpack.phone.translator.models

interface ITranslationModel {

    var allowDownloadingOveMobile : Boolean
        get() = false
        set(value) {

        }

    val translationType: TranslationType

    fun isLocaleSupported(locale: String): Boolean

    suspend fun translate(text: String, sourceLocale: String, targetLocale: String): String

    suspend fun needToDownloadModel(): Boolean = false

    suspend fun downloadModelIfNeeded(onProgress: (String) -> Unit = { }) = true

    fun cancelDownloadingModel() {

    }

    fun release() {

    }
}