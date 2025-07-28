package com.mobilerpgpack.phone.translator.models

import android.content.Context

class MLKitTranslationModel(
    private val context: Context,
    private var sourceLocale: String,
    private var targetLocale: String,
    private val allowDownloadingOverMobile: Boolean = false,
) : TranslationModel(context, allowDownloadingOverMobile) {

    override val supportedLocales: Collection<String>
        get() = TODO("Not yet implemented")

    override val translationType: TranslationType = TranslationType.MLKit
    override fun initialize(sourceLocale: String, targetLocale: String) {
        TODO("Not yet implemented")
    }

    override suspend fun translate(text: String, sourceLocale: String, targetLocale: String): TranslationResult {
        TODO("Not yet implemented")
    }

    override suspend fun needToDownloadModel(): Boolean {
        TODO("Not yet implemented")
    }

}