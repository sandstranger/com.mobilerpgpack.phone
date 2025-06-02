package com.mobilerpgpack.phone.utils

import android.content.Intent
import android.os.Environment

fun getPathFromIntent (intent: Intent?) : String {
    intent?.data?.also { uri ->
        val pattern = Regex("[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}")
        val storageDir = Environment.getExternalStorageDirectory()
        val storagePath = storageDir.absolutePath
        val modifiedStoragePath = "/storage"
        val pathSegment = uri.lastPathSegment
        val currentGamePath = if (pattern.containsMatchIn(pathSegment ?: "")) {
            modifiedStoragePath + "/" + pathSegment?.replace(":", "/")
        } else {
            storagePath + "/" + pathSegment?.replace("primary:", "")
        }

        return currentGamePath
    }
    return ""
}