package com.mobilerpgpack.phone.utils

import android.content.Context
import android.content.res.AssetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val GAME_FILES_ASSETS_FOLDER = "game_files"

class AssetExtractor (private val context: Context) {

    var assetsCopied = false
        private set

    suspend fun copyAssetsContentToInternalStorage () = withContext(Dispatchers.IO){
        if (assetsCopied){
            return@withContext
        }
        copyAssetsFolderToInternalStorage( GAME_FILES_ASSETS_FOLDER, context.getExternalFilesDir("")!!)
        assetsCopied = true
    }

    private fun copyAssetsFolderToInternalStorage(assetsFolder: String, destFolder: File) {
        val assetManager = context.assets
        try {
            val files = assetManager.list(assetsFolder)
            if (files != null) {
                if (!destFolder.exists()) {
                    destFolder.mkdirs()
                }
                for (filename in files) {
                    val assetPath = if (assetsFolder.isEmpty()) filename else "$assetsFolder/$filename"
                    val outFile = File(destFolder, filename)

                    val subFiles = assetManager.list(assetPath)
                    if (subFiles != null && subFiles.isNotEmpty()) {
                        copyAssetsFolderToInternalStorage( assetPath, outFile)
                    } else {
                        val shouldCopy = !outFile.exists() || !compareAssetAndFileSize(assetManager, assetPath, outFile)
                        if (shouldCopy) {
                            assetManager.open(assetPath).use { inputStream ->
                                FileOutputStream(outFile).use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun compareAssetAndFileSize(assetManager: AssetManager, assetPath: String, file: File): Boolean {
        return try {
            assetManager.openFd(assetPath).use { assetFileDescriptor ->
                val assetFileSize = assetFileDescriptor.length
                val fileSize = file.length()
                return assetFileSize == fileSize
            }
        } catch (e: IOException) {
            false
        }
    }
}