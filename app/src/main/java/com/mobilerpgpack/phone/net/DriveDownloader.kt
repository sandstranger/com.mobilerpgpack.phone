package com.mobilerpgpack.phone.net

import android.content.Context
import android.util.Log
import com.mobilerpgpack.phone.R
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
import java.io.IOException
import kotlin.coroutines.resumeWithException

class DriveDownloader(
    private val context: Context,
    private val apiKey: String
) {
    private val client = OkHttpClient()

    suspend fun download(fileId: String, destPath: String, onProgress: (String) -> Unit = { }) =
        withContext(Dispatchers.IO) {
            val bytesText = context.getString(R.string.bytes_text)
            val downloadedText = context.getString(R.string.downloaded_text)
            val unknownSizeText = context.getString(R.string.unknown_size)

            val TAG = "DriveDownload"

            val url = "https://www.googleapis.com/drive/v3/files/$fileId?alt=media&key=$apiKey"
            val request = Request.Builder().url(url).get().build()

            onProgress("$downloadedText: 0 $bytesText ($unknownSizeText)")

            val resp: Response = suspendCancellableCoroutine { cont ->
                val call = client.newCall(request)
                cont.invokeOnCancellation { call.cancel() }
                call.enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: Call, e: java.io.IOException) {
                        Log.d(
                            TAG,
                            "❌ OkHttp connection failed: ${e.javaClass.simpleName}: ${e.message}",
                            e
                        )
                        if (cont.isCancelled) {
                            Log.d(
                                TAG,
                                "🛑 Coroutine already cancelled, dropping error: ${e.message}"
                            )
                            return
                        }
                        cont.resumeWithException(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        cont.resume(response) { _, _, _ -> }
                    }
                })
            }

            if (!resp.isSuccessful) {
                resp.close()
                Log.d(TAG, "HTTP ${resp.code}: ${resp.message}")
                Log.d(TAG, url)
            }

            val contentLength = resp.body?.contentLength() ?: -1L

            if (contentLength > 0) {
                onProgress("$downloadedText: 0% (0 / $contentLength $bytesText)")
            } else {
                onProgress("$downloadedText: 0 $bytesText ($unknownSizeText)")
            }

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
                                val downloadProgress =
                                    "$downloadedText: $progress% ($downloadedBytes / $contentLength $bytesText)"
                                lastLoggedProgress = progress
                                Log.d(TAG, downloadProgress)
                                onProgress(downloadProgress)
                            }
                        } else {
                            val progress =
                                "$downloadedText: $downloadedBytes $bytesText ($unknownSizeText)"
                            Log.d(TAG, progress)
                            onProgress(progress)
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