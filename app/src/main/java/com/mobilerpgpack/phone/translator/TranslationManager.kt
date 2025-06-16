package com.mobilerpgpack.phone.translator

import android.content.Context
import android.content.res.Resources
import android.os.Build
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.mobilerpgpack.phone.engine.EngineTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val SourceLocale = "en"

object TranslationManager {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var wasInit = false
    private var _activeEngine : EngineTypes = EngineTypes.DefaultActiveEngine
    private var _isModelLoading = false
    private lateinit var currentLocale : String
    private lateinit var db: TranslationDatabase
    private val loadedTranslations : HashMap<String, TranslationEntry> = hashMapOf()
    private var mlKitTranslator : Translator? = null
    private var loadingTask: Job? = null

    val isModelLoading : Boolean
        get() {
            return _isModelLoading
        }

    var activeEngine : EngineTypes = EngineTypes.DefaultActiveEngine
        set(value) {
            _activeEngine = value
        }

    fun init (context: Context){
        if (wasInit){
            return
        }
        wasInit = true
        db = TranslationDatabase.getInstance(context)
        setLocale(getSystemLocale())
    }

    fun setLocale (newLocale : String){
        currentLocale = newLocale
        mlKitTranslator = buildMlkitTranslator()
        scope.cancel()
        scope.launch {
            loadSavedTranslations()
        }
    }

    fun terminate(){
        db.close()
        loadedTranslations.clear()
        mlKitTranslator?.close()
        scope.cancel()
    }

    fun downloadModel(useMobileNetwork : Boolean = false, onModelDownloaded : () -> Unit = { }){
        if (mlKitTranslator == null){
            onModelDownloaded()
            return
        }

        _isModelLoading = true
        val downloadConditions = if (useMobileNetwork) DownloadConditions.Builder().build() else
            DownloadConditions.Builder().requireWifi().build()

         loadingTask = scope.launch {
             mlKitTranslator?.downloadModelIfNeeded(downloadConditions)
                 ?.addOnSuccessListener {
                     loadingTask = null
                     _isModelLoading = false
                     onModelDownloaded()
                 }
                 ?.addOnFailureListener { e ->
                     loadingTask = null
                     _isModelLoading = false
                     onModelDownloaded()
                 }?.await()
         }
    }

    fun cancelDownloadModel() {
        _isModelLoading = false
        loadingTask?.cancel()
    }

    private suspend fun loadSavedTranslations (){
        loadedTranslations.clear()
        val entries = db.translationDao().getAllTranslations();

        entries.forEach {
            if (it.lang == currentLocale && it.engine == activeEngine){
                loadedTranslations[it.key] = it
            }
        }
    }

    private fun getSystemLocale(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales.get(0).language
        } else {
            Resources.getSystem().configuration.locale.language
        }
    }

    private fun buildMlkitTranslator () : Translator? {

        mlKitTranslator?.close()

        val sourceLang = TranslateLanguage.fromLanguageTag(SourceLocale)
        val targetLang = TranslateLanguage.fromLanguageTag(currentLocale)

        if (sourceLang != null && targetLang != null) {

            val options = TranslatorOptions.Builder()
                .setSourceLanguage(sourceLang)
                .setTargetLanguage(targetLang)
                .build()

            return Translation.getClient(options)
        }

        return null
    }
}