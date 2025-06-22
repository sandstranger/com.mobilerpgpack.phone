package com.mobilerpgpack.phone.translator.models

import android.content.Context
import android.util.Log
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.utils.isWifiConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class TranslationModel (private val context : Context,
                                 private val allowDownloadingOverMobile : Boolean = false ){
    private var currentDownload: Deferred<Boolean>? = null
    private val downloadMutex = Mutex()

    @Volatile
    var wasInitialize = false
        protected set

    protected val lockObject = Any()
    protected val scope = CoroutineScope(Dispatchers.IO)

    abstract val translationType : TranslationType

    open var allowDownloadingOveMobile : Boolean = false

    abstract fun initialize(sourceLocale: String, targetLocale : String)

    abstract suspend fun translate(text: String, sourceLocale: String, targetLocale : String) : String

    abstract suspend fun needToDownloadModel () : Boolean

    init {
        this@TranslationModel.allowDownloadingOveMobile = allowDownloadingOverMobile
    }

    open fun release(){
        wasInitialize = false
        cancelDownloadingModel()
        scope.coroutineContext.cancelChildren()
    }

    fun cancelDownloadingModel(){
        currentDownload?.cancel()
        currentDownload = null
    }

    suspend fun downloadModelIfNeeded(onProgress: (String) -> Unit = { }): Boolean {

        if (!needToDownloadModel()){
            return true
        }

        if (!allowDownloading()){
            return false
        }

        // Получаем (или создаём) Deferred<Boolean> под мьютексом
        val task: Deferred<Boolean> = downloadMutex.withLock {
            // Если есть незавершённый таск — переиспользуем
            currentDownload?.takeIf { !it.isCompleted }?.let { return@withLock it }

            // Иначе создаём новый
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