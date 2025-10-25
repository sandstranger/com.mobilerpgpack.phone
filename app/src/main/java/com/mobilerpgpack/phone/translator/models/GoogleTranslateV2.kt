package com.mobilerpgpack.phone.translator.models

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.mobilerpgpack.phone.main.KoinModulesProvider.Companion.COROUTINES_TRANSLATION_SCOPE
import com.mobilerpgpack.phone.utils.isInternetAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import org.koin.java.KoinJavaComponent.getKoin

class GoogleTranslateV2 (private val context : Context) : ITranslationModel {
    private val supportedLocales = hashSetOf("af","sq","am","ar","hy","az","eu","be","bn","bs","bg","ca","ceb", "zh",
        "zh-CN","zh-TW","co","hr","cs","da","nl","en","eo","et","fi","fr","fy","gl","ka","de","el","gu","ht","ha","haw","he",
        "hi","hmn","hu","is","ig","id","ga","it","ja","jw","kn","kk","km","ko","ku","ky","lo","la","lv","lt","lb","mk","mg",
        "ms","ml","mt","mi","mr","mn","my","ne","no","ny","ps","fa","pl","pt","pa","ro","ru","sm","gd","sr","st","sn","sd",
        "si","sk","sl","so","es","su","sw","sv","tl","tg","ta","te","th","tr","uk","ur","uz","vi","cy","xh","yi","yo","zu")

    private val scope : CoroutineScope = get(CoroutineScope::class.java,
        named(COROUTINES_TRANSLATION_SCOPE))

    private val retrofit : Retrofit = getKoin().get { parametersOf("https://translate.googleapis.com/") }

    private val translateService = retrofit.create(GoogleTranslateApi::class.java)

    override val translationType: TranslationType = TranslationType.GoogleTranslate

    override fun isLocaleSupported(locale: String): Boolean {
        return supportedLocales.contains(locale)
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
            googleTranslateV2(text, sourceLocale, targetLocale)
        }

        return TranslationResult(deferred.await(),true)
    }

    override fun release() {
        super.release()
        scope.coroutineContext.cancelChildren()
    }

    private suspend fun googleTranslateV2(
        text: String,
        sourceLocale: String,
        targetLocale: String
    ): String {
        return try {
            val response = translateService.translate(
                sourceLang = sourceLocale,
                targetLang = targetLocale,
                text = text
            )
            parseTranslation(response)
        } catch (e: Exception) {
            "Translation error: ${e.message}"
        }
    }

    private fun parseTranslation(rawResponse: List<Any>): String {
        return try {
            val mainList = rawResponse[0] as? List<*>
            mainList?.joinToString("") { sentence ->
                (sentence as? List<*>)?.firstOrNull()?.toString() ?: ""
            } ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private interface GoogleTranslateApi {
        @GET("translate_a/single")
        @Headers(
            "User-Agent: Mozilla/5.0",
            "Accept: application/json"
        )
        suspend fun translate(
            @Query("client") client: String = "gtx",
            @Query("dt") dataType: String = "t",
            @Query("sl") sourceLang: String,
            @Query("tl") targetLang: String,
            @Query("q") text: String
        ): List<Any>
    }

    private data class Sentence(
        @SerializedName("trans") val translation: String
    )
}