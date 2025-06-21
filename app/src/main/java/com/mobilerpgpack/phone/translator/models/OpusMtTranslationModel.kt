package com.mobilerpgpack.phone.translator.models

import android.content.Context
import com.mobilerpgpack.ctranslate2proxy.OpusMtTranslator
import com.mobilerpgpack.phone.translator.models.TranslationModel
import com.mobilerpgpack.phone.translator.TranslationType

class OpusMtTranslationModel(
    private val context : Context,
    private val pathToTranslationModel: String,
    private val pathToSourceProcessor: String,
    private val pathToTargetProcessor: String
) : TranslationModel(context) {
    private val opusMtTranslator : OpusMtTranslator =
        OpusMtTranslator(pathToTranslationModel, pathToSourceProcessor, pathToTargetProcessor)

    override val translationType: TranslationType = TranslationType.OpusMt

    override fun initialize(sourceLocale: String, targetLocale : String) =
        opusMtTranslator.initialize()

    override suspend fun translate(
        text: String,
        sourceLocale: String,
        targetLocale: String
    ): String = opusMtTranslator.translate(text, sourceLocale, targetLocale)

    override fun release() {
        super.release()
        opusMtTranslator.release()
    }

    override suspend fun needToDownloadModel(): Boolean = false
}