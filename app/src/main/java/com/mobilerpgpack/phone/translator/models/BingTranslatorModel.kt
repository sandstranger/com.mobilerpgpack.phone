package com.mobilerpgpack.phone.translator.models

import android.content.Context
import com.mobilerpgpack.phone.main.KoinModulesProvider.Companion.COROUTINES_TRANSLATION_SCOPE
import com.mobilerpgpack.phone.utils.isInternetAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get

class BingTranslatorModel(private val context: Context) : ITranslationModel {
    private val supportedLocales = hashSetOf(
        "auto-detect", "af", "ar", "bn", "bs", "bg", "yue", "ca", "zh-Hans", "zh-Hant", "hr", "cs", "da", "nl", "en",
        "et", "fj", "fil", "fi", "fr", "de", "el", "ht", "he", "hi", "mww", "hu", "is", "id", "it", "ja", "sw", "tlh",
        "tlh-Qaak", "ko", "lv", "lt", "mg", "ms", "mt", "nb", "fa", "pl", "pt", "otq", "ro", "ru", "sm", "sr-Cyrl",
        "sr-Latn", "sk", "sl", "es", "sv", "ty", "ta", "te", "th", "to", "tr", "uk", "ur", "vi", "cy", "yua"
    )

    private val scope : CoroutineScope = get(CoroutineScope::class.java, named(COROUTINES_TRANSLATION_SCOPE))

    private val translator : BingTranslatorEndPoint = get (BingTranslatorEndPoint::class.java)

    override val translationType: TranslationType
        get() = TranslationType.BingTranslate

    override fun isLocaleSupported(locale: String): Boolean {
        return supportedLocales.contains(locale)
    }

    override fun release() {
        super.release()
        scope.coroutineContext.cancelChildren()
    }

    override suspend fun translate(
        text: String,
        sourceLocale: String,
        targetLocale: String
    ): TranslationResult {
        if (!isLocaleSupported(targetLocale) || !context.isInternetAvailable()){
            return TranslationResult(text, false)
        }

        val deferred = scope.async {
            return@async translator.translate(text, sourceLocale, targetLocale)
        }

        return TranslationResult(deferred.await(),true)
    }
}