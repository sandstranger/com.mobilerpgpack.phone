package com.mobilerpgpack.phone.translator.models

import android.content.Context
import com.mobilerpgpack.ctranslate2proxy.OpusMtTranslator
import com.mobilerpgpack.phone.translator.models.TranslationModel
import com.mobilerpgpack.phone.translator.models.TranslationType

class OpusMtTranslationModel(
    private val context : Context,
    private val pathToTranslationModel: String,
    private val pathToSourceProcessor: String,
    private val pathToTargetProcessor: String
) : TranslationModel(context) {
    private val opusMtTranslator : OpusMtTranslator =
        OpusMtTranslator(pathToTranslationModel, pathToSourceProcessor, pathToTargetProcessor)

    override val translationType: TranslationType = TranslationType.OpusMt

    override fun initialize(sourceLocale: String, targetLocale : String){
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
    ): String  {
        initialize(sourceLocale, targetLocale)
        return opusMtTranslator.translate(text, sourceLocale, targetLocale)
    }

    override fun release() {
        synchronized(lockObject) {
            super.release()
            opusMtTranslator.release()
        }
    }

    override suspend fun needToDownloadModel(): Boolean = false
}