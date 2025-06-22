package com.mobilerpgpack.phone.translator.models

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import com.mobilerpgpack.ctranslate2proxy.Translator
import com.mobilerpgpack.phone.net.DriveDownloader
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.computeSHA256
import com.mobilerpgpack.phone.utils.unzipArchive
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import java.io.File

abstract class BaseM2M100TranslationModel(
    private val context: Context,
    private val allowDownloadingOverMobile: Boolean = false
) : TranslationModel(context, allowDownloadingOverMobile) {

    protected abstract val zipFileId: String
    protected abstract val zipFileSha256: String
    protected abstract val zipFileName: String
    protected abstract val needToDownloadModelPrefsKey: Preferences.Key<Boolean>
    protected abstract val translator: Translator

    private val pathToModelZipFile: String
        get() =
        "${context.getExternalFilesDir("")}${File.separator}$zipFileName"

    @Volatile
    protected var isModelDownloaded = false

    override fun initialize(sourceLocale: String, targetLocale: String) {
        if (isModelDownloaded && !wasInitialize) {
            synchronized(lockObject) {
                translator.initialize()
                wasInitialize = true
            }
        }
    }

    override fun release() {
        synchronized(lockObject) {
            super.release()
            translator.release()
        }
    }

    override suspend fun translate(
        text: String,
        sourceLocale: String,
        targetLocale: String
    ): String {
        if (isModelDownloaded) {
            val deferred = scope.async {
                initialize(sourceLocale, targetLocale)
                translator.translate(text, sourceLocale, targetLocale)
            }
            return deferred.await()
        }
        return text
    }

    override suspend fun downloadModelTask(onProgress: (String) -> Unit): Boolean {
        super.downloadModelTask(onProgress)
        if (isModelDownloaded) {
            return true
        }

        val modelZipFile = File(pathToModelZipFile)

        if (extractDownloadedModel(modelZipFile)){
            return true
        }

        modelDownloader.download(zipFileId, pathToModelZipFile, onProgress)
        return extractDownloadedModel(modelZipFile)
    }

    override suspend fun needToDownloadModel(): Boolean {
        return PreferencesStorage.getBooleanValue(context, needToDownloadModelPrefsKey, true).first()
    }

    private suspend fun extractDownloadedModel(zipFile: File): Boolean {
        try {
            if (zipFileSha256 == computeSHA256(zipFile) &&
                unzipArchive(pathToModelZipFile, context.getExternalFilesDir("")!!.absolutePath)
            ) {
                PreferencesStorage.setBooleanValue(context, needToDownloadModelPrefsKey, false)
                isModelDownloaded = true
                return true
            }
            return false
        } finally {
            zipFile.delete()
        }

        return false
    }

    private companion object {
        private val modelDownloader = DriveDownloader("AIzaSyCz-HWRD4hzUHB4aVEj6927ZjgTj-147PE")
    }
}