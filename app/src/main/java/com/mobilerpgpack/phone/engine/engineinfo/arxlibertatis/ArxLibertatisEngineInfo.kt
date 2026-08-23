package com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis

import android.content.Context
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.sun.jna.Native
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File
import kotlin.collections.contains

class ArxLibertatisEngineInfo(mainEngineLib: String, allLibs: Array<String>) :
    SDL3EngineInfo(mainEngineLib, allLibs, EngineTypes.ArxLibertatis) {
    private val arxPreferenceStorage by inject<ArxLibertatisPreferenceStorage>(
        named(EngineTypes.ArxLibertatis.name))
    private val context : Context by inject ()
    private val pathToUserFolder by lazy {
        super.pathToRootUserFolder + File.separator + "ArxLibertatis"
    }
    private val textureCacheDir by lazy { File(context.cacheDir,"axrx_libertatis_texture_cache") }

    override val pathToResource: String get() = arxPreferenceStorage.pathToArxFatalisFolder.value!!
    override val commandLineParams get() = arxPreferenceStorage.arxLibertatisCommandLineArgs.value!!
    override val touchFullScreenModeCanBeUsed = false
    override val targetGLESVersion= GLES_300_VERSION
    override val enableNGGL4ESSimpleShaderConv = true
    override val gl4esShaderCacheFolderName = "arx_libertatis_gl4es_cache"

    private external fun updateScreenControlsHidingState (controlsHided : Boolean)
    private external fun setPathToResources (pathToResources : String)
    private external fun setTextureData(enableETC2TextureSupport: Boolean, pathToTextureCacheDir : String)

    override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        textureCacheDir.mkdirs()
    }

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(ArxLibertatisEngineInfo::class.java, mainLibraryName)
        arxPreferenceStorage.apply {
            updateScreenControlsHidingState(hideScreenControls.value!!)
            setTextureData(enableEtc2TextureSupport.value!!,textureCacheDir.absolutePath)
        }
        setPathToResources(pathToResource)
    }

    override fun buildCustomCommandLineArgs () : Collection<String>{
        return mutableListOf<String>().apply {
            val pathResource = pathToResource
            if (pathResource.isNotEmpty() && File(pathResource).exists()) {
                this += DATA_DIR_COMMAND
                this += pathResource
            }

            this += USER_DIR_COMMAND
            this += pathToUserFolder

            this += CONFIGS_DIR_COMMAND
            this += pathToUserFolder
        }
    }

    private companion object {
        private const val DATA_DIR_COMMAND = "--data-dir"
        private const val USER_DIR_COMMAND = "--user-dir"
        private const val CONFIGS_DIR_COMMAND = "--config-dir"
    }
}