package com.mobilerpgpack.phone.utils

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.security.MessageDigest

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

fun getPathFromIntent (intent: Intent?) : String {
    intent?.data?.also { uri ->
        return getPathFromUri(uri)
    }
    return ""
}

fun computeSHA256(pathToFile : String): String {
    return FileInputStream(File(pathToFile)).use { inputStream ->
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

private fun getPathFromUri(uri: Uri?): String {

    val isTreeUri = DocumentsContract.isTreeUri(uri)
    val docId = if (isTreeUri) {
        DocumentsContract.getTreeDocumentId(uri)
    } else {
        DocumentsContract.getDocumentId(uri)
    }

    val parts = docId.split(":")
    if (parts.isEmpty()) return ""

    val type = parts[0]
    val relativePath = if (parts.size > 1) parts[1] else ""

    return when {
        type.equals("primary", ignoreCase = true) -> {
            "${Environment.getExternalStorageDirectory()}/$relativePath"
        }
        type.startsWith("raw") -> {
            relativePath
        }
        else -> {
            "/storage/$type/$relativePath"
        }
    }
}