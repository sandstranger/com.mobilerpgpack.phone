package com.mobilerpgpack.phone.utils

import android.app.Activity
import android.content.Intent
import android.view.KeyEvent
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.isResourceCorrect
import com.mobilerpgpack.phone.main.SDL2_NATIVE_LIB_NAME
import com.sun.jna.Function
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.reflect.Field
import java.security.MessageDigest

data class KeyCodeInfo (val keyCodeName : String, val keyCode : Int)

private const val KEYCODE_PREFIX = "KEYCODE_"
private const val UNKNOWN_KEYCODE = 0

private val keyCodesArray = arrayOfNulls<Any?>(1)
private val translateKeycodeNativeDelegate by lazy {
    Function.getFunction(SDL2_NATIVE_LIB_NAME, "TranslateKeycode")
}

val keyCodeMap : Map<Int, KeyCodeInfo> by lazy {
    KeyEvent::class.java.fields
        .filter { it.name.startsWith(KEYCODE_PREFIX) && isValidKeyCode(it)  }
        .sortedBy { it.name }
        .associate { field ->
            val keyCode = field.getInt(null)
            return@associate keyCode to KeyCodeInfo(field.name.replace(KEYCODE_PREFIX, ""),
            keyCode)
        }
}

fun startGame(activity: Activity, engineToPlay: EngineTypes) {
    val assetsExtractor: IAssetExtractor = get(IAssetExtractor::class.java)

    if (!assetsExtractor.assetsCopied) {
        activity.showErrorDialogBox(R.string.resources_not_ready)
        return
    }
    val activeEngineInfo: IEngineInfo = get (IEngineInfo::class.java,
        named(engineToPlay.toString()))
    activity.also {
        if (activeEngineInfo.isResourceCorrect(it)) {
            it.startActivity(activeEngineInfo.gameActivityClazz)
        }
    }
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

private fun isValidKeyCode (keyCodeField : Field) : Boolean{
    keyCodesArray[0] = keyCodeField.getInt(null)
    return translateKeycodeNativeDelegate.invokeInt(keyCodesArray) != UNKNOWN_KEYCODE
}