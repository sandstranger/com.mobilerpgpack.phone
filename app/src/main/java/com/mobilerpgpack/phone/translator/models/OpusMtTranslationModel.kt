package com.mobilerpgpack.phone.translator.models

import com.mobilerpgpack.ctranslate2proxy.OpusMtTranslator
import com.mobilerpgpack.phone.main.KoinModulesProvider.Companion.COROUTINES_TRANSLATION_SCOPE
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.utils.AssetExtractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get

class OpusMtTranslationModel() : ITranslationModel {
    @Volatile
    private var wasInitialize = false

    private val scope : CoroutineScope = get(CoroutineScope::class.java,
        named(COROUTINES_TRANSLATION_SCOPE))

    private val lockObject = Any()

    private val opusMtTranslator : OpusMtTranslator = get(OpusMtTranslator::class.java)

    override val translationType: TranslationType = TranslationType.OpusMt

    override fun isLocaleSupported(locale: String): Boolean {
        return locale == TranslationManager.RUSSIAN_LOCALE
    }

    private fun initialize(){
        if (wasInitialize || !AssetExtractor.assetsCopied){
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
        if (!isLocaleSupported(targetLocale) || !AssetExtractor.assetsCopied){
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