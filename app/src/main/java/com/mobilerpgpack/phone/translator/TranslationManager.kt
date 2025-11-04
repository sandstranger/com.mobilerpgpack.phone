package com.mobilerpgpack.phone.translator

import android.content.res.Resources
import android.os.Build
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.KoinModulesProvider.Companion.ACTIVE_TRANSLATION_MODEL_KEY
import com.mobilerpgpack.phone.main.KoinModulesProvider.Companion.TARGET_LOCALE_NAMES_KEY
import com.mobilerpgpack.phone.main.TRANSLATOR_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.translator.models.ITranslationModel
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.translator.sql.TranslationDatabase
import com.mobilerpgpack.phone.translator.sql.TranslationEntry
import com.sun.jna.Callback
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.cancellation.CancellationException

private fun interface IsTextTranslatedCallback : Callback {
    fun getTextPtr(ptr: Pointer, length: Int): Boolean
}

private fun interface TranslateTextCallback : Callback {
    fun translate(ptr: Pointer, length: Int, isTextFromDialogBox: Boolean)
}

private fun interface GetTranslatedTextCallback : Callback {
    fun getTranslation(ptr: Pointer, length: Int): String
}

private interface TranslationNativeBridge : Library {
    fun registerIsTranslatedDelegate(cb: IsTextTranslatedCallback)
    fun registerTranslateDelegate(cb: TranslateTextCallback)
    fun registerGetTranslationDelegate(cb: GetTranslatedTextCallback)
}

class TranslationManager : KoinComponent {

    private var _activeEngine: EngineTypes = EngineTypes.DefaultActiveEngine

    @Volatile
    private var translationModel : ITranslationModel = get (named(ACTIVE_TRANSLATION_MODEL_KEY))

    private val targetLocale : String = get(named(TARGET_LOCALE_NAMES_KEY))

    private val db: TranslationDatabase = get()

    private val scope : CoroutineScope = get ()

    private val intervalsTranslator : IntervalMarkerTranslator = get()

    private val translationModels : Map<TranslationType, ITranslationModel> = get()

    private val loadedTranslations = ConcurrentHashMap<String, TranslationEntry>()

    private val activeTranslations: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())

    private val activeTranslationsAwaitable = ConcurrentHashMap<String, Job>()

    private val isTranslatedCb : IsTextTranslatedCallback
    private val translateCb : TranslateTextCallback
    private val getTranslationCb : GetTranslatedTextCallback

    var inGame = false

    var activeEngine: EngineTypes
        get() = _activeEngine
        set(value) {
            if (_activeEngine == value) {
                return
            }
            _activeEngine = value

            scope.launch {
                reloadSavedTranslations()
            }
        }

    var allowDownloadingOveMobile: Boolean = false
        set(value) {
            translationModels.values.forEach {
                it.allowDownloadingOveMobile = value
            }
        }

    var activeTranslationType : TranslationType
        get() {
            return translationModel.translationType
        }set(value) {
            changeTranslationModel(value)
        }

    init {
        isTranslatedCb = IsTextTranslatedCallback { input,length ->
            isTranslated(input.getByteArray(0,length)) }
        translateCb = TranslateTextCallback { input,length, isTextFromDialogBox -> translate(
            input.getByteArray(0,length), isTextFromDialogBox) }
        getTranslationCb = GetTranslatedTextCallback { input,length ->
            getTranslation(input.getByteArray(0,length)) }

        val translatorLib = Native.load(TRANSLATOR_NATIVE_LIB_NAME, TranslationNativeBridge::class.java)
        translatorLib.registerIsTranslatedDelegate (isTranslatedCb)
        translatorLib.registerTranslateDelegate (translateCb)
        translatorLib.registerGetTranslationDelegate (getTranslationCb)
    }

    fun terminate() {
        db.close()
        activeTranslations.clear()
        loadedTranslations.clear()
        translationModels.values.forEach {
            it.release()
        }
    }

    suspend fun downloadModelIfNeeded(onProgress: (String) -> Unit = { }) {
        if (isTargetLocaleSupported()){
            translationModel.downloadModelIfNeeded(onProgress)
        }
    }

    fun isTranslationSupportedAsFlow(): Flow<Boolean> = flow {
        while (currentCoroutineContext().isActive) {
            emit(isTranslationSupported())
            delay(500)
        }
    }.distinctUntilChanged()

    fun isTargetLocaleSupported () : Boolean = translationModel.isLocaleSupported(targetLocale)

    suspend fun isTranslationSupported() : Boolean =
        isModelDownloaded() && isTargetLocaleSupported() && targetLocale != sourceLocale

    suspend fun isModelDownloaded () = !translationModel.needToDownloadModel()

    fun cancelDownloadModel() = translationModel.cancelDownloadingModel()

    fun getTranslation(input: ByteArray) : String {
        val text = input.sanitizeUtf8BytesToString()
        return if (isTranslated(text)) loadedTranslations[text]!!.value else text
    }

    fun getTranslation(text: String) =
        if (isTranslated(text)) loadedTranslations[text]!!.value else text

    fun isTranslated(input: ByteArray) =
        loadedTranslations.containsKey(input.sanitizeUtf8BytesToString())

    fun isTranslated(text : String) = loadedTranslations.containsKey(text)

    fun translate(input: ByteArray, textCameFromDialog : Boolean ): String {
        val text = input.sanitizeUtf8BytesToString()

        if (sourceLocale == targetLocale) {
            return text
        }

        if (isTranslated(text)) {
            return getTranslation(text)
        }

        if (activeTranslations.contains(text) || activeTranslationsAwaitable.containsKey(text)) {
            return text
        }

        scope.launch {
            translateAsync(text, textCameFromDialog)
        }
        return text
    }

    fun translate(text: String, onTextTranslated: (String) -> Unit, textCameFromDialog : Boolean = false) {
        if (sourceLocale == targetLocale){
            onTextTranslated(text)
            return
        }

        if (isTranslated(text)) {
            onTextTranslated(getTranslation(text))
            return
        }

        if (activeTranslations.contains(text) || activeTranslationsAwaitable.containsKey(text)) {
            onTextTranslated(text)
            return
        }

        scope.launch {
            onTextTranslated(translateAsync(text, textCameFromDialog))
        }
    }

    suspend fun translateAsync(text: String, textCameFromDialog : Boolean = false ): String = coroutineScope  {
        if (sourceLocale == targetLocale){
            return@coroutineScope text
        }

        val activeTranslationType = this@TranslationManager.activeTranslationType

        suspend fun saveTranslatedText(translatedText: String) {
            val translationEntry = TranslationEntry(
                key = text,
                lang = targetLocale,
                value = translatedText,
                engine = _activeEngine,
                translationModelType = activeTranslationType
            )
            db.translationDao().insertTranslation(translationEntry)
            loadedTranslations[text] = translationEntry
        }

        if (isTranslated(text)) {
            return@coroutineScope getTranslation(text)
        }

        if (!isTranslationSupported()){
            return@coroutineScope text
        }

        if (inGame) {
            if (activeTranslations.contains(text)) {
                return@coroutineScope text
            }
            activeTranslations.add(text)
        }
        else{
            val job = coroutineContext.job
            val existing = activeTranslationsAwaitable.putIfAbsent(text, job)
            existing?.join()
            if (isTranslated(text)) {
                return@coroutineScope getTranslation(text)
            }
        }

        try {
            val (translatedText, saveTextToSqlForced) = intervalsTranslator.translateWithFixedInterval (text,
                textCameFromDialog, inGame, _activeEngine) {
                cleanText -> translationModel.translate(cleanText, sourceLocale, targetLocale)
            }
            if (saveTextToSqlForced && activeTranslationType==this@TranslationManager.activeTranslationType) {
                saveTranslatedText(translatedText)
                return@coroutineScope translatedText
            }
            return@coroutineScope text
        }
        catch (ce: CancellationException) {
            throw ce
        }
        catch (_: Exception) {
            return@coroutineScope text
        } finally {
            activeTranslations.remove(text)
            activeTranslationsAwaitable.remove(text)
        }
    }

    private suspend fun loadSavedTranslations() {
        loadedTranslations.clear()
        val entries = db.translationDao().getAllTranslations();

        entries.forEach {
            if (it.lang == targetLocale && it.engine == activeEngine &&
                activeTranslationType == it.translationModelType) {
                loadedTranslations[it.key] = it
            }
        }
    }

    private suspend fun reloadSavedTranslations() {
        activeTranslations.clear()
        activeTranslationsAwaitable.clear()
        loadSavedTranslations()
    }

    private fun changeTranslationModel (targetTranslationType : TranslationType){
        if (activeTranslationType != targetTranslationType) {
            translationModel.release()
            translationModel = translationModels[targetTranslationType]!!
            scope.launch {
                reloadSavedTranslations()
            }
        }
    }

    companion object{
        const val RUSSIAN_LOCALE = "ru"

        const val ENGLISH_LOCALE = "en"

        const val sourceLocale = ENGLISH_LOCALE

        fun getSystemLocale(): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Resources.getSystem().configuration.locales.get(0).language
            } else {
                Resources.getSystem().configuration.locale.language
            }
        }
    }
}