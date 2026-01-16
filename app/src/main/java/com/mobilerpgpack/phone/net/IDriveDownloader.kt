package com.mobilerpgpack.phone.net

import java.io.File

interface IDriveDownloader{
    suspend fun download(fileId: String, destFile: File, onProgress: (String) -> Unit = { })
}