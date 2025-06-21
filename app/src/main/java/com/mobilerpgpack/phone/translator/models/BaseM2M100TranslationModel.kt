package com.mobilerpgpack.phone.translator.models

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.mobilerpgpack.ctranslate2proxy.M2M100Translator
import com.mobilerpgpack.ctranslate2proxy.Translator
import com.mobilerpgpack.phone.net.DriveDownloader
import com.mobilerpgpack.phone.translator.TranslationType
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.computeSHA256
import com.mobilerpgpack.phone.utils.unzipArchive
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

abstract class BaseM2M100TranslationModel(
    private val context: Context,
    private val allowDownloadingOverMobile : Boolean = false
) : TranslationModel(context,allowDownloadingOverMobile) {

    protected abstract val zipFileId : String
    protected abstract val zipFileSha256 : String
    protected abstract val zipFileName : String
    protected abstract val isModelDownloadedPrefsKey : Preferences.Key<Boolean>
    protected abstract val translator : Translator

    private val pathToModelZipFile : String = "${context.getExternalFilesDir("")}${File.separator}$zipFileName"

    private var isModelDownloaded = false
    override val translationType: TranslationType = TranslationType.M2M100

    init {
        runBlocking {
            isModelDownloaded = !needToDownloadModel()
        }
    }

    override fun initialize(sourceLocale: String, targetLocale: String) {
        if (isModelDownloaded){
            translator.initialize()
        }
    }

    override fun release() {
        super.release()
        translator.release()
    }

    override suspend fun translate(
        text: String,
        sourceLocale: String,
        targetLocale: String
    ): String {
        if (isModelDownloaded) {
            return translator.translate(text,sourceLocale,targetLocale)
        }
        return text
    }

    override suspend fun downloadModelTask() {
        super.downloadModelTask()
        if (isModelDownloaded){
            return
        }

        val modelZipFile = File(pathToModelZipFile)
        if (modelZipFile.exists()){
            modelZipFile.delete()
        }
        modelDownloader.download(zipFileId, pathToModelZipFile)

        try {
            if (zipFileSha256 == computeSHA256(pathToModelZipFile) &&
                unzipArchive(pathToModelZipFile, context.getExternalFilesDir("")!!.absolutePath)){
                PreferencesStorage.setBooleanValue(context, isModelDownloadedPrefsKey, true)
                isModelDownloaded = true
                initialize("","")
            }
        }
        finally {
            modelZipFile.delete()
        }
    }

    override suspend fun needToDownloadModel(): Boolean {
        return PreferencesStorage.getBooleanValue(context,isModelDownloadedPrefsKey, true).first()!!
    }

    private companion object{
        private val modelDownloader = DriveDownloader("AIzaSyCz-HWRD4hzUHB4aVEj6927ZjgTj-147PE")
    }
}