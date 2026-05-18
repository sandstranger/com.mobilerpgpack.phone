package com.mobilerpgpack.phone.translator.models

import android.content.Context
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.isWifiConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.getValue

abstract class TranslationModel (private val context : Context,
                                 allowDownloadingOverMobile : Boolean = false ) :
    KoinComponent, ITranslationModel {
    private var currentDownload: Deferred<Boolean>? = null
    private val downloadMutex = Mutex()

    @Volatile
    var wasInitialize = false
        protected set

    protected val lockObject = Any()
    protected val scope : CoroutineScope by inject (
        named(KoinModulesProvider.BACKGROUND_THREAD_COROUTINE_KEY))

    protected abstract val supportedLocales : Collection<String>

    abstract override val translationType : TranslationType

    override var allowDownloadingOveMobile : Boolean = false

    protected abstract fun initialize(sourceLocale: String, targetLocale : String)

    abstract override suspend fun translate(text: String, sourceLocale: String, targetLocale : String) : TranslationResult

    abstract override suspend fun needToDownloadModel () : Boolean

    init {
        this@TranslationModel.allowDownloadingOveMobile = allowDownloadingOverMobile
    }

    override fun release(){
        wasInitialize = false
        cancelDownloadingModel()
        scope.coroutineContext.cancelChildren()
    }

    override fun isLocaleSupported (locale: String) : Boolean{
        return supportedLocales.contains(locale)
    }

    override fun cancelDownloadingModel(){
        currentDownload?.cancel()
        currentDownload = null
    }

    override suspend fun downloadModelIfNeeded(onProgress: (String) -> Unit): Boolean {
        if (!needToDownloadModel()){
            return true
        }

        if (!allowDownloading()){
            return false
        }

        val task: Deferred<Boolean> = downloadMutex.withLock {
            currentDownload?.takeIf { !it.isCompleted }?.let { return@withLock it }

            val newTask = scope.async {
                try {
                    downloadModelTask(onProgress)
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

    protected open suspend fun downloadModelTask(onProgress: (String) -> Unit = { }) : Boolean {
        return true
    }

    private fun allowDownloading () = this@TranslationModel.allowDownloadingOveMobile || context.isWifiConnected()
}