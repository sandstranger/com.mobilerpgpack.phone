package com.mobilerpgpack.phone.translator.models

import com.google.mlkit.nl.translate.TranslateLanguage
import com.mobilerpgpack.ctranslate2proxy.OpusMtTranslator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren

class OpusMtTranslationModel(
    private val pathToTranslationModel: String,
    private val pathToSourceProcessor: String,
    private val pathToTargetProcessor: String,
) : ITranslationModel {

    @Volatile
    private var wasInitialize = false

    private val scope = CoroutineScope(Dispatchers.IO)
    private val lockObject = Any()

    private val opusMtTranslator : OpusMtTranslator =
        OpusMtTranslator(pathToTranslationModel, pathToSourceProcessor, pathToTargetProcessor)

    override val translationType: TranslationType = TranslationType.OpusMt

    override fun isLocaleSupported(locale: String): Boolean {
        return locale == TranslateLanguage.RUSSIAN
    }

    private fun initialize(){
        if (wasInitialize){
            return
        }
        synchronized(lockObject) {
            wasInitialize = true
            opusMtTranslator.initialize()
        }
    }

    override suspend fun translate(
        text: String,
        sourceLocale: String,
        targetLocale: String
    ): TranslationResult {
        if (!isLocaleSupported(targetLocale)){
            return TranslationResult(text,false)
        }
        val deferred = scope.async {
            initialize()
            opusMtTranslator.translate(text,sourceLocale,targetLocale)
        }

        return TranslationResult(deferred.await(),true)
    }

    override fun release() {
        synchronized(lockObject) {
            super.release()
            scope.coroutineContext.cancelChildren()
            opusMtTranslator.release()
        }
    }

    override suspend fun needToDownloadModel(): Boolean = false
}