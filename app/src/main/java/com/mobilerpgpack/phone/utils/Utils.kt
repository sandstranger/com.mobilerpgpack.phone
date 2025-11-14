package com.mobilerpgpack.phone.utils

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import kotlinx.coroutines.flow.first
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.security.MessageDigest

suspend fun startGame(context: Context, engineToPlay: EngineTypes) {

    val assetsExtractor: IAssetExtractor = get(IAssetExtractor::class.java)

    if (!assetsExtractor.assetsCopied) {
        return
    }

    val activeEngineInfo: IEngineInfo = get (IEngineInfo::class.java,
        named(engineToPlay.toString()))

    val pathToResource = activeEngineInfo.pathToResource.first()

    if (pathToResource.isEmpty() || !File(pathToResource).exists()) {
        MaterialDialog(context).show {
            title(R.string.error)
            message(R.string.can_not_start_engine)
            positiveButton(R.string.ok_text)
        }
        return
    }

    context.startActivity(activeEngineInfo.gameActivityClazz)
}

fun unzipArchive(zipPath: String, destDir: String) : Boolean {
    try {
        val zipFile = ZipFile(zipPath)
        zipFile.extractAll(destDir)
        return true
    } catch (e: ZipException) {
        e.printStackTrace()
        return false
    }
}

fun computeSHA256(file: File): String {
    if (!file.exists()){
        return ""
    }

    return FileInputStream(file).use { inputStream ->
        computeSHA256(inputStream).joinToString("") { "%02x".format(it) }
    }
}

fun computeSHA256(inputStream: InputStream): ByteArray {
    val digest = MessageDigest.getInstance("SHA-256")
    val buffer = ByteArray(4096)
    var bytesRead: Int
    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
        digest.update(buffer, 0, bytesRead)
    }
    return digest.digest()
}