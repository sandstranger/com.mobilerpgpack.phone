package com.mobilerpgpack.phone.translator

import com.mobilerpgpack.ctranslate2proxy.OpusMtTranslator

class OpusMtTranslationModel(
    private val pathToTranslationModel: String,
    private val pathToSourceProcessor: String,
    private val pathToTargetProcessor: String
) : TranslationModel() {
    private val opusMtTranslator : OpusMtTranslator =
        OpusMtTranslator(pathToTranslationModel,pathToSourceProcessor, pathToTargetProcessor)

    override val translationType: TranslationType = TranslationType.OpusMt

    override fun initialize() = opusMtTranslator.initialize()

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