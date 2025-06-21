package com.mobilerpgpack.phone.translator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class TranslationModel{
    private var currentDownload: Deferred<Boolean>? = null
    private val downloadMutex = Mutex()

    protected val scope = CoroutineScope(Dispatchers.IO)

    abstract val translationType : TranslationType

    open var allowDownloadingOveMobile : Boolean = false

    abstract fun initialize()

    abstract suspend fun translate(text: String, sourceLocale: String, targetLocale : String) : String

    abstract suspend fun needToDownloadModel () : Boolean

    open fun release(){
        cancelDownloadingModel()
        scope.coroutineContext.cancelChildren()
    }

    fun cancelDownloadingModel(){
        currentDownload?.cancel()
        currentDownload = null
    }

    suspend fun downloadModelIfNeeded(): Boolean {
        // Получаем (или создаём) Deferred<Boolean> под мьютексом
        val task: Deferred<Boolean> = downloadMutex.withLock {
            // Если есть незавершённый таск — переиспользуем
            currentDownload?.takeIf { !it.isCompleted }?.let { return@withLock it }

            // Иначе создаём новый
            val newTask = scope.async {
                try {
                    downloadModelTask()
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

    protected open suspend fun downloadModelTask(){
    }
}