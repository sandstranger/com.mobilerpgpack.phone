package com.mobilerpgpack.phone.net

interface IDriveDownloader{
    suspend fun download(fileId: String, destPath: String, onProgress: (String) -> Unit = { })
}