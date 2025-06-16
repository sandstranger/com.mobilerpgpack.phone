package com.mobilerpgpack.phone.translator

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.utils.isInternetAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val SourceLocale = "en"

@SuppressLint("StaticFieldLeak")
object TranslationManager {
    private var wasInit = false
    private var _activeEngine : EngineTypes = EngineTypes.DefaultActiveEngine
    private var _isModelLoading = false
    private var _allowDownloadingOveMobile = false
    private var mlKitTranslator : Translator? = null
    private var loadingTask: Deferred<Boolean>? = null
    private lateinit var currentLocale : String
    private lateinit var db: TranslationDatabase
    private lateinit var languageIdentifier : LanguageIdentifier
    private lateinit var downloadConditions : DownloadConditions
    private lateinit var context: Context

    private val scope = CoroutineScope(Dispatchers.Default)
    private val loadedTranslations : HashMap<String, TranslationEntry> = hashMapOf()
    private val activeTranslations : HashSet<String> = hashSetOf()
    private val modelCache = mutableMapOf<String, TranslateRemoteModel>()

    val isModelLoading : Boolean
        get() {
            return _isModelLoading
        }

    var activeEngine : EngineTypes = EngineTypes.DefaultActiveEngine
        set(value) {
            _activeEngine = value
        }

    var allowDownloadingOveMobile : Boolean = false
        set(value) {
            if (_allowDownloadingOveMobile!=value){
                downloadConditions = buildConditions()
            }
            _allowDownloadingOveMobile = value
        }

    fun init (context: Context){
        if (wasInit){
            return
        }
        this.context = context
        wasInit = true
        downloadConditions = buildConditions()
        languageIdentifier = LanguageIdentification.getClient()
        db = TranslationDatabase.getInstance(context)
        setLocale(getSystemLocale())
    }

    fun terminate(){
        db.close()
        activeTranslations.clear()
        loadedTranslations.clear()
        cancelDownloadModel()
        mlKitTranslator?.close()
        scope.cancel()
    }

    fun setLocale (newLocale : String){
        scope.launch {
            setLocaleAsync(newLocale)
        }
    }

    suspend fun downloadModelIfNeeded() : Boolean{
        if (mlKitTranslator == null){
            return false
        }

        if (_isModelLoading){
            return loadingTask!!.await()
        }

        val modelManager = RemoteModelManager.getInstance()

        val isDownloaded = modelManager.isModelDownloaded(getRemoteModel(currentLocale)).await()
        if (isDownloaded) {
            return true
        }

        if (!context.isInternetAvailable()){
            return false
        }

        _isModelLoading = true

        loadingTask = scope.async {
            _isModelLoading = true
            try {
                mlKitTranslator?.downloadModelIfNeeded(downloadConditions)?.await()
                return@async true
            } catch (_: Exception) {
                return@async false
            } finally {
                _isModelLoading = false
            }
        }

        return loadingTask!!.await()
    }

    fun cancelDownloadModel() {
        _isModelLoading = false
        loadingTask?.cancel()
    }

    @JvmStatic
    fun isTranslated (text: String) = loadedTranslations.contains(text)

    @JvmStatic
    fun getTranslation(key: String) =
        if (isTranslated(key)) loadedTranslations[key]!!.value else key

    @JvmStatic
    fun translate (text: String ){
        if (isTranslated(text) || activeTranslations.contains(text)){
            return
        }

        scope.launch {
            translateAsync(text)
        }
    }

    fun translate (text: String, onTextTranslated : (String) -> Unit){
        if (isTranslated(text)){
            onTextTranslated (getTranslation(text))
            return
        }

        if (activeTranslations.contains(text)){
            onTextTranslated(text)
            return
        }

        scope.launch {
            onTextTranslated(translateAsync(text))
        }
    }

    suspend fun translateAsync(text: String): String {

        if (isTranslated(text)){
            return getTranslation(text)
        }

        if (activeTranslations.contains(text)){
            return text
        }

        activeTranslations.add(text)

        var langCode : String = currentLocale

        try {
            langCode = languageIdentifier.identifyLanguage(text).await()
        } catch (_: Exception) {
            activeTranslations.remove(text)
            val translationEntry = TranslationEntry(
                key = text,
                lang = currentLocale,
                value = text,
                engine = _activeEngine )
            loadedTranslations[text] = translationEntry
            return text
        }

        if (currentLocale != langCode){
            setLocaleAsync(langCode)
        }

        if (isTranslated(text)){
            return getTranslation(text)
        }

        if (mlKitTranslator == null || !downloadModelIfNeeded()){
            return text
        }

        try {
            val translatedValue = mlKitTranslator!!.translate(text).await()
            val translationEntry = TranslationEntry(
                key = text,
                lang = currentLocale,
                value = translatedValue,
                engine = _activeEngine )
            loadedTranslations[text] = translationEntry
            db.translationDao().insertTranslation(translationEntry)
            return translatedValue
        } catch (_: Exception) {
            return text
        }
        finally {
            activeTranslations.remove(text)
        }
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

    private fun buildConditions(): DownloadConditions {
        return if (_allowDownloadingOveMobile)
            DownloadConditions.Builder().build()
        else
            DownloadConditions.Builder().requireWifi().build()
    }

    private suspend fun setLocaleAsync (newLocale : String){
        currentLocale = newLocale
        cancelDownloadModel()
        activeTranslations.clear()
        mlKitTranslator?.close()
        mlKitTranslator = buildMlkitTranslator()
        loadSavedTranslations()
    }

    private fun getRemoteModel(langCode: String): TranslateRemoteModel {
        return modelCache.getOrPut(langCode) {
            TranslateRemoteModel.Builder(langCode).build()
        }
    }
}