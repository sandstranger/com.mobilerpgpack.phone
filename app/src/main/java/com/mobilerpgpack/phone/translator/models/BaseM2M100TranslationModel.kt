package com.mobilerpgpack.phone.translator.models

import android.content.Context
import android.util.Log
import com.mobilerpgpack.ctranslate2proxy.Translator
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.main.RESOURCES_DOWNLOADER_ID
import com.mobilerpgpack.phone.net.IDriveDownloader
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.computeSHA256
import com.mobilerpgpack.phone.utils.unzipArchive
import com.opentouchgaming.saffal.FileSAF
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File

abstract class BaseM2M100TranslationModel(
    context: Context,
    private val pathToModelFolder: String,
    private val spmFile: String,
    allowDownloadingOverMobile: Boolean = false
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

    private val modelDownloader : IDriveDownloader by inject { parametersOf(RESOURCES_DOWNLOADER_ID) }
    private val zipFile : File by inject { parametersOf("${modelFolder.name}.zip") }
    private val modelFolder : File by inject { parametersOf(pathToModelFolder) }
    private val smpFile : File by inject { parametersOf(spmFile) }
    private val userDirectory : File by inject (named(KoinModulesProvider.EXTERNAL_STORAGE_DIRECTORY_KEY))
    private val assetsExtractor : IAssetExtractor = get ()

    @Volatile
    protected var isModelDownloaded = false

    init {
        assetsExtractor.assetsStartedCopyListeners += { onAssetsStartedCopy() }
        assetsExtractor.assetsFinishCopyListeners += { onAssetsFinishCopy() }
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

        modelFolder.mkdirs()
        if (extractDownloadedModel(zipFile)){
            return true
        }

        modelDownloader.download(zipFileId, zipFile, onProgress)

        return extractDownloadedModel(zipFile)
    }

    override suspend fun needToDownloadModel(): Boolean {
        if (this.isModelDownloaded){
            return false
        }
        val needToDownloadModel = !modelFolder.exists() || !smpFile.exists() || zipFile.exists();
        isModelDownloaded = !needToDownloadModel
        return needToDownloadModel
    }

    private fun extractDownloadedModel(zipFile: File): Boolean {
        isModelDownloaded = unzipArchive(zipFile,userDirectory.absolutePath,zipFileSha256)
        return isModelDownloaded
    }

    private fun onAssetsStartedCopy () {
        isModelDownloaded = false
    }

    private fun onAssetsFinishCopy (){
        scope.launch { needToDownloadModel() }
    }
}