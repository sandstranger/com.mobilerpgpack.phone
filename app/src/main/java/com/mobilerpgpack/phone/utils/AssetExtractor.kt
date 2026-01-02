package com.mobilerpgpack.phone.utils

import android.content.Context
import android.content.res.AssetManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AssetExtractor : IAssetExtractor, KoinComponent {

    private val preferencesStorage : PreferencesStorage by inject ()

    private val context : Context by inject ()

    private val assetToIgnoreChecking : Collection<String> = get (
        named(ASSETS_TO_IGNORE_CHECKING_COLLECTION_NAME))

    @Volatile
    private var assetsCopying = false

    private var _assetsCopied by mutableStateOf(false)

    private val pathToUserFolder get() = preferencesStorage.pathToRootUserFolder

    private var alwaysCopyAllFiles = false

    override val assetsCopied get() = _assetsCopied

    override val assetsStartedCopyListeners = MulticastAction()

    override val assetsFinishCopyListeners = MulticastAction()

    override suspend fun copyAssetsContentToInternalStorage (){
        if (assetsCopying){
            return
        }
        assetsCopying = true
        _assetsCopied = false
        waitUntil { !preferencesStorage.prefsWasLoaded }
        with(File(pathToUserFolder)){
            if (!exists()){
                mkdirs()
            }

            assetsStartedCopyListeners.invoke()
            try {
                alwaysCopyAllFiles = getAlwaysCopyFilesCurrentState()
                copyAssetsFolderToInternalStorage( GAME_FILES_ASSETS_FOLDER, this)
            }
            finally {
                assetsFinishCopyListeners.invoke()
                _assetsCopied = true
                assetsCopying = false
            }
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

        if (!alwaysCopyAllFiles && assetToIgnoreChecking.any { assetPath.contains(it) }){
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
        val assetsVersionFile = File ( "$pathToUserFolder${File.separator}${ASSETS_VERSION_FILE_NAME}")
        fun writeDefaultVersionToVersionsFile () =
            assetsVersionFile.writeText(Json.encodeToString(AssetsVersionProvider(
                ASSETS_CURRENT_VERSION)))

        if (!assetsVersionFile.exists()){
            writeDefaultVersionToVersionsFile()
            return true
        }

        try {
            val assetsVersionProvider = Json.decodeFromString<AssetsVersionProvider>(assetsVersionFile.readText())

            val copyAssetsForced = assetsVersionProvider.assetsVersion != ASSETS_CURRENT_VERSION
            if (copyAssetsForced) {
                writeDefaultVersionToVersionsFile()
            }

            return copyAssetsForced
        }
        catch (_ : Exception){
            writeDefaultVersionToVersionsFile()
            return true
        }
    }

    companion object{
        const val ASSETS_TO_IGNORE_CHECKING_COLLECTION_NAME = "assets_to_ignore"

        private const val GAME_FILES_ASSETS_FOLDER = "game_files"

        private const val ASSETS_CURRENT_VERSION = 5

        private const val ASSETS_VERSION_FILE_NAME = "AssetsCurrentVersion.json"

        @Serializable
        private data class AssetsVersionProvider (val assetsVersion : Int)
    }
}