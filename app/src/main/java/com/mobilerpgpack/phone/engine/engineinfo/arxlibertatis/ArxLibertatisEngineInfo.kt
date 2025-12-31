package com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.sun.jna.Native
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class ArxLibertatisEngineInfo(mainEngineLib: String, allLibs: Array<String>) :
    SDL2EngineInfo(mainEngineLib, allLibs, EngineTypes.ArxLibertatis) {

    private val arxPreferenceStorage by inject<ArxLibertatisPreferenceStorage>(
        named(EngineTypes.ArxLibertatis.name))

    private val pathToUserFolder by lazy {
        super.pathToRootUserFolder + File.separator + "ArxLibertatis"
    }

    override val pathToResource: String get() = arxPreferenceStorage.pathToArxFatalisFolder

    override val commandLineParams get() = arxPreferenceStorage.arxLibertatisCommandLineArgs

    override val touchFullScreenModeCanBeUsed = false

    override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs
            return with(mutableListOf<String>()) {
                this += baseCommandLineArgs

                val pathResource = pathToResource
                if (!baseCommandLineArgs.contains(DATA_DIR_COMMAND) &&
                    pathResource.isNotEmpty() && File(pathResource).exists()
                ) {
                    this += DATA_DIR_COMMAND
                    this += pathResource
                }

                this += USER_DIR_COMMAND
                this += pathToUserFolder

                this += CONFIGS_DIR_COMMAND
                this += pathToUserFolder

                this.toTypedArray()
            }
        }

    private external fun updateScreenControlsHidingState (controlsHided : Boolean)

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(ArxLibertatisEngineInfo::class.java, mainLibraryName)
        updateScreenControlsHidingState(preferencesStorage.hideScreenControls)
    }

    private companion object {
        private const val DATA_DIR_COMMAND = "--data-dir"
        private const val USER_DIR_COMMAND = "--user-dir"
        private const val CONFIGS_DIR_COMMAND = "--config-dir"
    }
}