package com.mobilerpgpack.phone.utils

import android.app.Activity
import android.util.Log
import android.view.KeyEvent
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.isResourceCorrect
import com.mobilerpgpack.phone.main.ONE_FRAME_DELAY
import com.mobilerpgpack.phone.main.SDL3_NATIVE_LIB_NAME
import com.opentouchgaming.saffal.FileSAF
import com.sun.jna.Native
import kotlinx.coroutines.delay
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Field
import java.security.MessageDigest

data class KeyCodeInfo (val keyCodeName : String, val keyCode : Int)

val keyCodeMap : Map<Int, KeyCodeInfo> by lazy { KeyStoreProvider.keyCodeMap }

fun startGame(activity: Activity, engineToPlay: EngineTypes) {
    val assetsExtractor: IAssetExtractor = get(IAssetExtractor::class.java)
    val activeEngineInfo: IEngineInfo = get (IEngineInfo::class.java,
        named(engineToPlay.toString()))
    if (!assetsExtractor.assetsCopied || !activeEngineInfo.engineReadyToStart) {
        activity.showErrorDialogBox(R.string.resources_not_ready)
        return
    }
    with(activity){
        if (activeEngineInfo.isResourceCorrect(this)) {
            startActivity(activeEngineInfo.gameActivityClazz)
        }
    }
}

suspend fun waitUntil (delegateToAwait : () -> Boolean ){
    while (delegateToAwait()){
        delay(ONE_FRAME_DELAY)
    }
}

fun unzipArchive(zipFile: File, destDir: String, zipFileSha256: String): Boolean {
    try {
        return zipFileSha256 == computeSHA256(zipFile) &&
                unzipArchive(zipFile, destDir)
    } finally {
        zipFile.delete()
    }
    return false
}

fun unzipArchive(zipFile: File, destDir: String) : Boolean {
    try {
        ZipFile(zipFile).extractAll(destDir)
        return true
    } catch (e: ZipException) {
        Log.e("ZipException", e.toString())
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

fun copyFolder(src: File, dst: FileSAF) {
    if (!src.exists()) return
    if (src.isDirectory) {
        if (!dst.exists()) dst.mkdirs()
        src.listFiles()?.forEach { file ->
            copyFolder(file, FileSAF(dst, file.name))
        }
    } else {
        try {
            src.inputStream().use { input ->
                dst.parentFile.mkdirs()
                if (!dst.exists()){
                    dst.createNewFile()
                }
                dst.outputStream.use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: IOException) {
            Log.e("Exception", e.toString())
        }
    }
}

private object KeyStoreProvider {
    private const val KEYCODE_PREFIX = "KEYCODE_"
    private const val UNKNOWN_KEYCODE = 0

    val keyCodeMap : Map<Int, KeyCodeInfo> by lazy {
        KeyEvent::class.java.fields
            .filter { it.name.startsWith(KEYCODE_PREFIX) && isValidKeyCode(it)  }
            .sortedBy { it.name }
            .associate { field ->
                return@associate field.getInt(null).run {
                    return@associate this to KeyCodeInfo(field.name.replace(KEYCODE_PREFIX, ""),
                        this)
                }
            }
    }

    private external fun TranslateKeycode(keyCode : Int) : Int

    init {
        Native.register(KeyStoreProvider::class.java, SDL3_NATIVE_LIB_NAME)
    }

    private fun isValidKeyCode (keyCodeField : Field) : Boolean{
        return TranslateKeycode(keyCodeField.getInt(null)) != UNKNOWN_KEYCODE
    }
}

