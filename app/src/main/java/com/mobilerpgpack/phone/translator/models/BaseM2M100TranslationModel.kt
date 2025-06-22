package com.mobilerpgpack.phone.translator.models

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.mobilerpgpack.ctranslate2proxy.Translator
import com.mobilerpgpack.phone.net.DriveDownloader
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.computeSHA256
import com.mobilerpgpack.phone.utils.unzipArchive
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

abstract class BaseM2M100TranslationModel(
    private val context: Context,
    private val pathToModelFolder: String,
    private val spmFile: String,
    private val allowDownloadingOverMobile: Boolean = false
) : TranslationModel(context, allowDownloadingOverMobile) {

    protected abstract val zipFileId: String
    protected abstract val zipFileSha256: String
    protected abstract val translator: Translator

    private val pathToModelZipFile by lazy {
        return@lazy "${context.getExternalFilesDir("")}${File.separator}${File(pathToModelFolder).name}.zip"
    }

    private val zipFile by lazy { File(pathToModelZipFile) }
    private val modelFolder by lazy { File(pathToModelFolder) }
    private val smpFile by lazy { File(spmFile) }

    @Volatile
    private var isModelDownloaded = false

    init {
        runBlocking {
            isModelDownloaded = !needToDownloadModel()
        }
    }

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
        if (isModelDownloaded){
            return false
        }
        return !modelFolder.exists() || !smpFile.exists() || zipFile.exists()
    }

    private fun extractDownloadedModel(zipFile: File): Boolean {
        try {
            if (zipFileSha256 == computeSHA256(zipFile) &&
                unzipArchive(pathToModelZipFile, context.getExternalFilesDir("")!!.absolutePath)
            ) {
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