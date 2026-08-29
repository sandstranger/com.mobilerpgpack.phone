package com.mobilerpgpack.phone.utils

import android.content.Context
import com.mobilerpgpack.phone.main.KoinModulesProvider
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
    private val userFolder: File by inject(named(KoinModulesProvider.ROOT_USER_DIRECTORY_KEY))
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
        userFolder.apply {
            mkdirs()
            withContext(Dispatchers.Main) { assetsStartedCopyListeners.invoke() }
            try {
                copyAssetsFolderToInternalStorage(GAME_FILES_ASSETS_FOLDER, this)
            } finally {
                withContext(Dispatchers.Main) {
                    preferencesStorage.setBooleanValueAsync(preferencesStorage.allAssetsCopiedPrefsKey, true)
                    preferencesStorage.setIntValueAsync(preferencesStorage.assetsCurrentVersionPrefsKey, ASSETS_CURRENT_VERSION)
                    assetsFinishCopyListeners.invoke()
                }
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
                    val assetPath =
                        if (assetsFolder.isEmpty()) filename else "$assetsFolder/$filename"
                    val outFile = File(destFolder, filename)

                    val subFiles = assetManager.list(assetPath)
                    if (subFiles != null && subFiles.isNotEmpty()) {
                        copyAssetsFolderToInternalStorage(assetPath, outFile)
                    } else {
                        assetManager.open(assetPath).use { inputStream ->
                            FileOutputStream(outFile).use { outputStream ->
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
        private const val ASSETS_CURRENT_VERSION = 42
    }
}