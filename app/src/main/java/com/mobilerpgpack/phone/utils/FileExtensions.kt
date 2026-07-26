package com.mobilerpgpack.phone.utils

import com.opentouchgaming.saffal.FileSAF

fun FileSAF.writeTextSafely(textToWrite: String) {
    parentFile.mkdirs()
    if (!exists()) {
        createNewFile()
    }
    outputStream.use { outputStream ->
        outputStream.write(textToWrite.toByteArray(Charsets.UTF_8))
    }
}