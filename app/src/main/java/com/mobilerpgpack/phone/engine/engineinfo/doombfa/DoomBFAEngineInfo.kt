package com.mobilerpgpack.phone.engine.engineinfo.doombfa

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.sun.jna.Native
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File

class DoomBFAEngineInfo(mainEngineLib: String, allLibs: Array<String>) :
    SDL3EngineInfo(mainEngineLib, allLibs, EngineTypes.Classic_RBDOOM_3_BFG) {
    private val homeDirectoryFolder : File by inject { parametersOf(HOME_DIRECTORY_NAME) }

    private val doomBFAPreferenceStorage by inject<DoomBFAPreferencesStorage>(
        named(EngineTypes.Classic_RBDOOM_3_BFG.name))

    override val pathToResource: String get() = doomBFAPreferenceStorage.pathToDoom3Resources.value!!

    override val preferencesStorage get() = doomBFAPreferenceStorage

    override val commandLineParams: String get() = doomBFAPreferenceStorage.commandLineArgs.value!!

    override val loadGL4ES = false

    override val touchFullScreenModeCanBeUsed = false

    override val targetGLESVersion = 320

    private external fun setPathsToResources (pathToHomeFolder : String, pathToResourcesFolder : String)

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(DoomBFAEngineInfo::class.java, mainLibraryName)
        setPathsToResources(homeDirectoryFolder.absolutePath,
            pathToResource)
    }

    private companion object{
        private const val HOME_DIRECTORY_NAME = "doombfa"
    }
}