package com.mobilerpgpack.phone.utils

import android.content.Context
import com.mobilerpgpack.phone.main.KoinModulesProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AssetExtractor : IAssetExtractor, KoinComponent {

    private val preferencesStorage : PreferencesStorage by inject ()

    private val context : Context by inject ()

    private val assetsVersionFile : File by inject { parametersOf(ASSETS_VERSION_FILE_NAME) }

    private val userFolder : File by inject (named(KoinModulesProvider.ROOT_USER_DIRECTORY_KEY))

    private val assetsInfo: AssetsInfo by lazy { getAssetsInfo() }

    @Volatile
    private var assetsCopying = false
    @Volatile
    private var _assetsCopied = true

    override val assetsCopied get() = _assetsCopied

    override val assetsStartedCopyListeners = MulticastAction()

    override val assetsFinishCopyListeners = MulticastAction()

    override fun clearSubscribers() {
        assetsStartedCopyListeners.clear()
        assetsFinishCopyListeners.clear()
    }

    override fun resetAssetsInfo() {
        assetsInfo.allAssetsCopied = false
        assetsVersionFile.writeTextSafely(Json.encodeToString(
            AssetsInfoProvider(assetsInfo.assetsVersion, false)))
    }

    override suspend fun copyAssetsContentToInternalStorage (){
        if (assetsCopying || assetsInfo.allAssetsCopied){
            return
        }
        assetsCopying = true
        _assetsCopied = false
        waitUntil { !preferencesStorage.prefsWasLoaded }
        userFolder.apply {
            mkdirs()
            withContext(Dispatchers.Main) { assetsStartedCopyListeners.invoke() }
            try {
                copyAssetsFolderToInternalStorage(GAME_FILES_ASSETS_FOLDER, this)
            }
            finally {
                assetsVersionFile.writeTextSafely(Json.encodeToString(
                    AssetsInfoProvider(assetsInfo.assetsVersion,true)))
                withContext(Dispatchers.Main) { assetsFinishCopyListeners.invoke() }
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
                        val shouldCopy = !outFile.exists() || assetsInfo.copyAllAssetsForced
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

    @JvmName("_getAssetsInfo")
    private fun getAssetsInfo () : AssetsInfo {
        assetsVersionFile.apply {
            mkdirs()
            fun writeDefaultAssetInfoToFile () =
                writeTextSafely(Json.encodeToString(AssetsInfoProvider(
                    ASSETS_CURRENT_VERSION, false)))

            if (!exists()){
                writeDefaultAssetInfoToFile()
                return defaultAssetsInfo
            }

            try {
                val assetsInfo = Json.decodeFromString<AssetsInfoProvider>(readText())

                val copyAssetsForced = assetsInfo.assetsVersion != ASSETS_CURRENT_VERSION
                if (copyAssetsForced) {
                    writeDefaultAssetInfoToFile()
                }

                return AssetsInfo(copyAssetsForced, ASSETS_CURRENT_VERSION,
                    if (copyAssetsForced) false else assetsInfo.allAssetsCopied)
            }
            catch (_ : Exception){
                writeDefaultAssetInfoToFile()
                return defaultAssetsInfo
            }
        }
    }

    companion object{
        private const val GAME_FILES_ASSETS_FOLDER = "game_files"

        private const val ASSETS_CURRENT_VERSION = 20

        private const val ASSETS_VERSION_FILE_NAME = "AssetsCurrentVersion.json"

        private val defaultAssetsInfo = AssetsInfo(true,
            ASSETS_CURRENT_VERSION, false)

        @Serializable
        private data class AssetsInfoProvider (val assetsVersion : Int, val allAssetsCopied : Boolean)

        private data class AssetsInfo (val copyAllAssetsForced : Boolean,
                                       val assetsVersion : Int, var allAssetsCopied : Boolean)
    }
}