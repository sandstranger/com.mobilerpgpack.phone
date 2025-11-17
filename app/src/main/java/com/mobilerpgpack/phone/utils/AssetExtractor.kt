package com.mobilerpgpack.phone.utils

import android.content.Context
import android.content.res.AssetManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AssetExtractor (private val context: Context,
                      private val assetFoldersToIgnoreChecking : Collection<String> = emptyList()) : IAssetExtractor {

    @Volatile
    private var assetsCopying = false

    private var _assetsCopied by mutableStateOf(false)

    private val userFolder = context.getExternalFilesDir("")!!

    private val pathToUserFolder = userFolder.absolutePath

    private val alwaysCopyAllFiles by lazy {
        getAlwaysCopyFilesCurrentState()
    }

    override val assetsCopied get() = _assetsCopied

    override val assetsStartedCopyListeners: MutableCollection<() -> Unit> = mutableListOf()

    override val assetsFinishCopyListeners: MutableCollection<() -> Unit> = mutableListOf()

    override suspend fun copyAssetsContentToInternalStorage () = withContext(Dispatchers.IO){
        if (assetsCopying){
            return@withContext
        }
        assetsCopying = true
        _assetsCopied = false
        assetsStartedCopyListeners.forEach { it.invoke() }
        try {
            copyAssetsFolderToInternalStorage( GAME_FILES_ASSETS_FOLDER,
                userFolder)
        }
        finally {
            _assetsCopied = true
            assetsCopying = false
            assetsFinishCopyListeners.forEach { it.invoke() }
        }
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

        if (!alwaysCopyAllFiles && assetFoldersToIgnoreChecking.any { assetPath.contains(it) }){
            return true
        }

        return try {
            assetManager.openFd(assetPath).use { assetFileDescriptor ->
                val assetFileSize = assetFileDescriptor.length
                val fileSize = file.length()
                return assetFileSize == fileSize
            }
        } catch (_: IOException) {
            false
        }
    }

    private fun getAlwaysCopyFilesCurrentState () : Boolean{
        val assetsVersionFile = File ("${pathToUserFolder}${File.separator}${ASSETS_VERSION_FILE_NAME}")

        val gson = Gson()

        fun writeDefaultVersionToVersionsFile () =
            assetsVersionFile.writeText(gson.toJson(AssetsVersionProvider(
                ASSETS_CURRENT_VERSION)))

        if (!assetsVersionFile.exists()){
            writeDefaultVersionToVersionsFile()
            return true
        }

        try {
            val assetsVersionProvider = gson.fromJson(assetsVersionFile.readText(),
                AssetsVersionProvider::class.java)
            return !ASSETS_CURRENT_VERSION.equals(assetsVersionProvider.assetsVersion, true)
        }
        catch (_ : Exception){
            writeDefaultVersionToVersionsFile()
            return true
        }
    }

    private companion object{
        private const val GAME_FILES_ASSETS_FOLDER = "game_files"

        private const val ASSETS_CURRENT_VERSION = "1.0"

        private const val ASSETS_VERSION_FILE_NAME = "AssetsCurrentVersion.json"

        private data class AssetsVersionProvider (val assetsVersion : String)
    }
}