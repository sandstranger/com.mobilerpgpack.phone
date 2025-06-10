package com.mobilerpgpack.phone.utils

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract

fun getPathFromIntent (intent: Intent?) : String {
    intent?.data?.also { uri ->
        return getPathFromUri(uri)
    }
    return ""
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