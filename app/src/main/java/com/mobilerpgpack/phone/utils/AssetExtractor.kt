package com.mobilerpgpack.phone.utils

import android.content.Context
import com.mobilerpgpack.phone.main.GAME_CONTROLLER_DB_NAME
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.opentouchgaming.saffal.FileSAF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AssetExtractor : IAssetExtractor, KoinComponent {
    private val preferencesStorage: PreferencesStorage by inject()
    private val context: Context by inject()
    private val userFolder: FileSAF by inject(named(KoinModulesProvider.ROOT_USER_DIRECTORY_KEY))
    private val externalStorageFolder : File by inject (named(KoinModulesProvider.EXTERNAL_STORAGE_DIRECTORY_KEY))
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

    override suspend fun copyAssetsContentToInternalStorage(copyForced: Boolean) {
        val needToCopyAssets = copyForced || !preferencesStorage.allAssetsCopied.value!! ||
                preferencesStorage.assetsCurrentVersion.value != ASSETS_CURRENT_VERSION
        if (!needToCopyAssets) {
            return
        }
        assetsCopying = true
        _assetsCopied = false
        waitUntil { !preferencesStorage.prefsWasLoaded }
        userFolder.mkdirs()
        externalStorageFolder.mkdirs()
        try {
            withContext(Dispatchers.Main) { assetsStartedCopyListeners.invoke() }
            copyAssetsFolderToInternalStorage(GAME_FILES_ASSETS_FOLDER, userFolder)
            copyAssetsFolderToInternalStorage(EXTERNAL_STORAGE_ASSETS_FOLDER, externalStorageFolder)
        }
        finally {
            withContext(Dispatchers.Main) {
                preferencesStorage.setBooleanValueAsync(preferencesStorage.allAssetsCopiedPrefsKey, true)
                preferencesStorage.setIntValueAsync(preferencesStorage.assetsCurrentVersionPrefsKey, ASSETS_CURRENT_VERSION)
                assetsFinishCopyListeners.invoke()
            }
            _assetsCopied = true
            assetsCopying = false
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
                    val assetPath =
                        if (assetsFolder.isEmpty()) filename else "$assetsFolder/$filename"
                    val outFile = FileSAF(destFolder, filename)
                    outFile.parentFile.mkdirs()

                    val subFiles = assetManager.list(assetPath)
                    if (subFiles != null && subFiles.isNotEmpty()) {
                        copyAssetsFolderToInternalStorage(assetPath, outFile)
                    } else {
                        if (!outFile.exists()) {
                            outFile.createNewFile()
                        }
                        assetManager.open(assetPath).use { inputStream ->
                            outFile.outputStream.use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private companion object {
        private const val GAME_FILES_ASSETS_FOLDER = "game_files"
        private const val EXTERNAL_STORAGE_ASSETS_FOLDER = "external_storage_files"
        private const val ASSETS_CURRENT_VERSION = 33
    }
}