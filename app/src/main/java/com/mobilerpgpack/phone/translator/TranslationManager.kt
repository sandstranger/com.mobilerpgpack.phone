package com.mobilerpgpack.phone.translator

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.mobilerpgpack.ctranslate2proxy.OpusMtTranslator
import com.mobilerpgpack.ctranslate2proxy.M2M100Translator
import com.mobilerpgpack.phone.engine.EngineTypes
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import java.io.File

object TranslationManager {
    private var wasInit = false
    private var _activeEngine : EngineTypes = EngineTypes.DefaultActiveEngine
    private var _allowDownloadingOveMobile = false
    private var mlKitTranslator : Translator? = null
    private var currentDownload: Deferred<Boolean>? = null
    private var _c2translateProxy : OpusMtTranslator? = null

    private lateinit var targetLocale : String
    private lateinit var db: TranslationDatabase
    private lateinit var downloadConditions : DownloadConditions
    private lateinit var pathToOptModel : String
    private lateinit var optModelSourceProcessor : String
    private lateinit var optModelTargetProcessor : String

    private val downloadMutex = Mutex()
    private val scope = TranslatorApp.globalScope
    private val loadedTranslations : HashMap<String, TranslationEntry> = hashMapOf()
    private val activeTranslations : HashSet<String> = hashSetOf()

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

    fun init (context: Context, allowDownloadingOveMobile : Boolean = false){
        if (wasInit){
            return
        }

        val filesRootDir = context.getExternalFilesDir("")!!
        pathToOptModel = "${filesRootDir.absolutePath}${File.separator}opus-ct2-en-ru"
        optModelSourceProcessor = "${pathToOptModel}${File.separator}source.spm"
        optModelTargetProcessor = "${pathToOptModel}${File.separator}target.spm"

        this._allowDownloadingOveMobile = allowDownloadingOveMobile
        wasInit = true
        downloadConditions = buildConditions()
        db = TranslationDatabase.getInstance(context)
        setLocale(getSystemLocale())
    }

    fun terminate(){
        db.close()
        activeTranslations.clear()
        loadedTranslations.clear()
        cancelDownloadModel()
        mlKitTranslator?.close()
    }

    fun setLocale (newLocale : String){
        scope.launch {
            setLocaleAsync(newLocale)
        }
    }

    suspend fun downloadModelIfNeeded(): Boolean {
        // Быстрый проход без блокировки: если уже скачано или транспортер не настроен
        val translator = mlKitTranslator ?: return false

        // Получаем (или создаём) Deferred<Boolean> под мьютексом
        val task: Deferred<Boolean> = downloadMutex.withLock {
            // Если есть незавершённый таск — переиспользуем
            currentDownload?.takeIf { !it.isCompleted }?.let { return@withLock it }

            // Иначе создаём новый
            val newTask = scope.async {
                try {
                    // сам вызов ML Kit
                    translator.downloadModelIfNeeded(downloadConditions).await()
                    true
                } catch (_: Exception) {
                    false
                }
            }
            currentDownload = newTask
            newTask
        }

        return try {
            task.await()
        } finally {
            downloadMutex.withLock {
                if (currentDownload === task) {
                    currentDownload = null
                }
            }
        }
    }

    @JvmStatic
    fun getTranslation(text: String ) : String {
        if (isTranslated(text)) {
            return loadedTranslations[text]!!.value
        }

        translate(text)
        return text
    }

    fun isTranslated (text: String) = loadedTranslations.contains(text)

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

        suspend fun saveTranslatedText (translatedText : String){
            val translationEntry = TranslationEntry(
                key = text,
                lang = targetLocale,
                value = translatedText,
                engine = _activeEngine )
            db.translationDao().insertTranslation(translationEntry)
            loadedTranslations[text] = translationEntry
        }

        if (isTranslated(text)){
            return getTranslation(text)
        }

        if (activeTranslations.contains(text)){
            return text
        }

        if (isTranslated(text)){
            return getTranslation(text)
        }

        if (targetLocale == TranslateLanguage.RUSSIAN) {
            activeTranslations.add(text)

            initC2TranslateIfNeeded()

            try {
                val translatedValue = _c2translateProxy!!.translate(text)
                saveTranslatedText(translatedValue)
                return translatedValue
            }
            catch (e : Exception){
                Log.d("EXCEPTION", e.toString())
                return text
            }
            finally {
                activeTranslations.remove(text)
            }
        }

        if (mlKitTranslator == null){
            return text
        }

        activeTranslations.add(text)

        downloadModelIfNeeded()

        try {
            val translatedValue = mlKitTranslator!!.translate(text).await()
            saveTranslatedText(translatedValue)
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
            if (it.lang == targetLocale && it.engine == activeEngine){
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

    private suspend fun setLocaleAsync (newLocale : String){
        targetLocale = newLocale
        rebuildAllContent()
    }

    private suspend fun rebuildAllContent (){
        cancelDownloadModel()
        activeTranslations.clear()
        mlKitTranslator?.close()
        mlKitTranslator = buildMlkitTranslator()
        loadSavedTranslations()
    }

    private fun translate (text: String ){
        if (isTranslated(text) || activeTranslations.contains(text)){
            return
        }

        scope.launch {
            translateAsync(text)
        }
    }

    private fun cancelDownloadModel() {
        currentDownload?.cancel()
        currentDownload = null
    }

    @Synchronized
    private fun initC2TranslateIfNeeded() {
        if (_c2translateProxy == null) {
            _c2translateProxy = OpusMtTranslator(pathToOptModel, optModelSourceProcessor,
                optModelTargetProcessor)
        }
    }
}