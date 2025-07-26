package com.mobilerpgpack.phone.translator.models

import android.content.Context
import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.mobilerpgpack.phone.translator.models.TranslationType
import kotlinx.coroutines.tasks.await

class MLKitTranslationModel(
    private val context: Context,
    private var sourceLocale: String,
    private var targetLocale: String,
    private val allowDownloadingOverMobile: Boolean = false,
) :
    TranslationModel(context, allowDownloadingOverMobile) {

    override val supportedLocales = hashSetOf("af","am","ar","ar-Latn","az","be","bg","bg-Latn","bn","bs","ca","ceb","co","cs","cy",
        "da","de","el","el-Latn","en","eo","es","et","eu","fa","fi","fil","fr","fy","ga","gd","gl","gu","ha","haw",
        "he","hi","hi-Latn","hmn","hr","ht","hu","hy","id","ig","is","it","ja","ja-Latn","jv","ka","kk","km",
        "kn","ko","ku","ky","la","lb","lo","lt","lv","mg","mi","mk","ml","mn","mr","ms","mt","my","ne","nl","no",
        "ny","pa","pl","ps","pt","ro","ru","ru-Latn","sd","si","sk","sl","sm","sn","so","sq","sr","st","su","sv","sw",
        "ta","te","tg","th","tr","uk","ur","uz","vi","xh","yi","yo","zh","zh-Latn","zu")

    private val modelCache = mutableMapOf<String, TranslateRemoteModel>()
    private var downloadConditions: DownloadConditions
    private var mlKitTranslator: Translator? = null

    override val translationType: TranslationType = TranslationType.MLKit

    override var allowDownloadingOveMobile: Boolean
        get() = super.allowDownloadingOveMobile
        set(value) {
            super.allowDownloadingOveMobile = value
            downloadConditions = buildConditions()
        }

    init {
        downloadConditions = buildConditions()
    }

    override fun initialize(sourceLocale: String, targetLocale: String) {
        if (wasInitialize) {
            return
        }

        synchronized(lockObject) {
            this.sourceLocale = sourceLocale
            this.targetLocale = targetLocale
            mlKitTranslator = buildMlkitTranslator()
            wasInitialize = true
        }
    }

    override suspend fun translate(
        text: String,
        sourceLocale: String,
        targetLocale: String
    ): TranslationResult {
        initialize(sourceLocale, targetLocale)
        if (!isLocaleSupported(targetLocale)){
            return TranslationResult(text,false)
        }
        return try {
            val translatedText = mlKitTranslator?.translate(text)?.await()
            return if (translatedText == null) TranslationResult(text, false) else
                TranslationResult(translatedText, true)
        } catch (e: Exception) {
            TranslationResult(text,false)
        }
    }

    override suspend fun downloadModelTask(onProgress: (String) -> Unit): Boolean {
        super.downloadModelTask(onProgress)
        initialize(sourceLocale, targetLocale)
        mlKitTranslator?.downloadModelIfNeeded(downloadConditions)?.await()
        return true
    }

    override suspend fun needToDownloadModel(): Boolean {
        val modelManager = RemoteModelManager.getInstance()
        val sourceLocaleModelDownloaded =
            modelManager.isModelDownloaded(getRemoteModel(sourceLocale)).await()
        val targetLocaleModelDownloaded =
            modelManager.isModelDownloaded(getRemoteModel(targetLocale)).await()
        return !sourceLocaleModelDownloaded || !targetLocaleModelDownloaded
    }

    override fun release() {
        synchronized(lockObject) {
            super.release()
            mlKitTranslator?.close()
            mlKitTranslator = null
        }
    }

    private fun buildMlkitTranslator(): Translator? {
        mlKitTranslator?.close()

        val sourceLang = TranslateLanguage.fromLanguageTag(sourceLocale)
        val targetLang = TranslateLanguage.fromLanguageTag(targetLocale)

        if (sourceLang != null && targetLang != null) {

            val options = TranslatorOptions.Builder()
                .setSourceLanguage(sourceLang)
                .setTargetLanguage(targetLang)
                .build()

            return Translation.getClient(options)
        }

        return null
    }

    private fun buildConditions(): DownloadConditions {
        return if (super.allowDownloadingOveMobile)
            DownloadConditions.Builder().build()
        else
            DownloadConditions.Builder().requireWifi().build()
    }

    private fun getRemoteModel(langCode: String): TranslateRemoteModel {
        return modelCache.getOrPut(langCode) {
            TranslateRemoteModel.Builder(langCode).build()
        }
    }
}