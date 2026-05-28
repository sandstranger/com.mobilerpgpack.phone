package com.mobilerpgpack.phone.net

import android.content.Context
import android.util.Log
import com.mobilerpgpack.phone.R
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DriveDownloader(private val apiKey: String) : KoinComponent, IDriveDownloader {

    private val context: Context by inject()
    private val client: OkHttpClient by inject()

    override suspend fun download(
        fileId: String,
        destFile: File,
        onProgress: (String) -> Unit
    ) {
        val bytesText = context.getString(R.string.bytes_text)
        val downloadedText = context.getString(R.string.downloaded_text)
        val unknownSizeText = context.getString(R.string.unknown_size)

        val url = "https://www.googleapis.com/drive/v3/files/$fileId?alt=media&key=$apiKey"
        val request = Request.Builder().url(url).get().build()

        onProgress("$downloadedText: 0 $bytesText ($unknownSizeText)")

        val resp: Response = suspendCancellableCoroutine { cont ->
            val call = client.newCall(request)

            cont.invokeOnCancellation {
                call.cancel()
            }

            call.enqueue(object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (cont.isCancelled) return
                    cont.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    cont.resume(response)
                }
            })
        }

        resp.use { response ->

            if (!response.isSuccessful) {
                Log.d(TAG, "HTTP ${response.code}: ${response.message}")
                Log.d(TAG, url)
                return
            }
            val body = response.body
            val contentLength = body.contentLength()

            if (contentLength > 0) {
                onProgress("$downloadedText: 0% (0 / $contentLength $bytesText)")
            } else {
                onProgress("$downloadedText: 0 $bytesText ($unknownSizeText)")
            }

            var downloadedBytes = 0L
            var lastLoggedProgress = 0
            destFile.parentFile?.mkdirs()
            FileOutputStream(destFile).use { out ->
                body.byteStream().use { input ->
                    val buf = ByteArray(8 * 1024)
                    while (currentCoroutineContext().isActive) {
                        val read = try {
                            input.read(buf)
                        } catch (e: IOException) {
                            if (e.message == "closed") break
                            throw e
                        }

                        if (read == -1) break

                        out.write(buf, 0, read)
                        downloadedBytes += read

                        if (contentLength > 0) {
                            val progress = (downloadedBytes * 100 / contentLength).toInt()
                            if (progress >= lastLoggedProgress + 5) {
                                lastLoggedProgress = progress
                                val msg =
                                    "$downloadedText: $progress% ($downloadedBytes / $contentLength $bytesText)"
                                Log.d(TAG, msg)
                                onProgress(msg)
                            }

                        } else {
                            val msg =
                                "$downloadedText: $downloadedBytes $bytesText ($unknownSizeText)"
                            Log.d(TAG, msg)
                            onProgress(msg)
                        }
                    }
                }
            }

            if (!currentCoroutineContext().isActive) {
                destFile.delete()
                Log.w(TAG, "⛔ Download cancelled")
            } else {
                Log.i(TAG, "✅ Downloaded: ${destFile.absolutePath}")
            }
        }
    }

    private companion object {
        private const val TAG = "DriveDownload"
    }
}