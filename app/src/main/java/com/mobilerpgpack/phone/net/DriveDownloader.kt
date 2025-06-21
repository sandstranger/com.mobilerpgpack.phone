package com.mobilerpgpack.phone.net

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resumeWithException

class DriveDownloader(
    private val apiKey: String
) {
    private val client = OkHttpClient()

    suspend fun download(fileId: String, destPath: String) = withContext(Dispatchers.IO) {
        val TAG = "DriveDownload"

        val url = "https://www.googleapis.com/drive/v3/files/$fileId?alt=media&key=$apiKey"
        val request = Request.Builder().url(url).get().build()

        val resp: Response = suspendCancellableCoroutine { cont ->
            val call = client.newCall(request)
            cont.invokeOnCancellation { call.cancel() }
            call.enqueue(object : okhttp3.Callback {
                override fun onFailure(call: Call, e: java.io.IOException) {
                    if (cont.isCancelled) return
                    cont.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    cont.resume(response) { _, _, _ -> }
                }
            })
        }

        if (!resp.isSuccessful) {
            resp.close()
            Log.d(TAG,"HTTP ${resp.code}: ${resp.message}")
            Log.d(TAG,url)
        }

        val contentLength = resp.body?.contentLength() ?: -1L
        var downloadedBytes = 0L
        var lastLoggedProgress = 0

        FileOutputStream(File(destPath)).use { out ->
            resp.body!!.byteStream().use { input ->
                val buf = ByteArray(8 * 1024)
                var read: Int = 0
                while (isActive && input.read(buf).also { read = it } != -1) {
                    out.write(buf, 0, read)
                    downloadedBytes += read

                    if (contentLength > 0) {
                        val progress = (downloadedBytes * 100 / contentLength).toInt()
                        if (progress >= lastLoggedProgress + 5) {
                            Log.d(TAG, "Downloading: $progress% ($downloadedBytes / $contentLength bytes)")
                            lastLoggedProgress = progress
                        }
                    } else {
                        Log.d(TAG, "Downloaded: $downloadedBytes bytes (Unknown size)")
                    }
                }
            }
        }

        if (!currentCoroutineContext().isActive) {
            File(destPath).delete()
            Log.w(TAG, "⛔ Downloading cancelled")
        } else {
            Log.i(TAG, "✅ Downloaded to: $destPath")
        }
    }
}