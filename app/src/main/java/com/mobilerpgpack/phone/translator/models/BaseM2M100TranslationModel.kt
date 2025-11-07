package com.mobilerpgpack.phone.translator.models

import android.content.Context
import android.util.Log
import com.mobilerpgpack.ctranslate2proxy.Translator
import com.mobilerpgpack.phone.net.DriveDownloader
import com.mobilerpgpack.phone.net.IDriveDownloader
import com.mobilerpgpack.phone.utils.computeSHA256
import com.mobilerpgpack.phone.utils.unzipArchive
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin
import java.io.File

abstract class BaseM2M100TranslationModel(
    private val context: Context,
    private val pathToModelFolder: String,
    private val spmFile: String,
    private val allowDownloadingOverMobile: Boolean = false
) : TranslationModel(context, allowDownloadingOverMobile), KoinComponent {

    protected abstract val zipFileId: String
    protected abstract val zipFileSha256: String
    protected abstract val translator: Translator

    override val supportedLocales: Collection<String> = hashSetOf("af", "am", "ar", "ast", "az", "ba", "be", "bg", "bn",
        "br", "bs", "ca", "ceb", "cs", "cy", "da", "de", "el", "en", "es", "et", "fa", "ff", "fi", "fr",
        "fy", "ga", "gd", "gl", "gu", "ha", "he", "hi", "hr", "ht", "hu", "hy", "id", "ig", "ilo", "is",
        "it", "ja", "jv", "ka", "kk", "km", "kn", "ko", "lb", "lg", "ln", "lo", "lt", "lv", "mg", "mk", "ml",
        "mn", "mr", "ms", "my", "ne", "nl", "no", "ns", "oc", "or", "pa", "pl", "ps", "pt", "ro", "ru", "sd",
        "si", "sk", "sl", "so", "sq", "sr", "ss", "su", "sv", "sw", "ta", "th", "tl", "tn", "tr", "uk", "ur",
        "uz", "vi", "wo", "xh", "yi", "yo", "zh", "zu")

    private val pathToModelZipFile by lazy {
        return@lazy "${context.getExternalFilesDir("")}${File.separator}${File(pathToModelFolder).name}.zip"
    }

    private val modelDownloader : IDriveDownloader = get { parametersOf("AIzaSyCz-HWRD4hzUHB4aVEj6927ZjgTj-147PE") }

    private val zipFile by lazy { File(pathToModelZipFile) }

    private val modelFolder by lazy { File(pathToModelFolder) }

    private val smpFile by lazy { File(spmFile) }

    @Volatile
    protected var isModelDownloaded = false

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
    ): TranslationResult {
        if (isLocaleSupported(targetLocale) && isModelDownloaded) {
            val deferred = scope.async {
                initialize(sourceLocale, targetLocale)
                translator.translate(text, sourceLocale, targetLocale)
            }
            return TranslationResult(deferred.await(), true)
        }
        return TranslationResult(text, false)
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
}