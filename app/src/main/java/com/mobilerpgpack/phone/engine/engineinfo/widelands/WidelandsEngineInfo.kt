package com.mobilerpgpack.phone.engine.engineinfo.widelands

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.doombfa.DoomBFAEngineInfo.Companion.HOME_DIRECTORY_NAME
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.sun.jna.Native
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import java.io.File
import kotlin.getValue

class WidelandsEngineInfo (mainEngineLib: String,
                           allLibs: Array<String>) :
    SDL3EngineInfo(mainEngineLib, allLibs, EngineTypes.Widelands) {
    val widelandsRootFolder : File by inject { parametersOf("widelands") }
    val widelandsDataFolder by lazy { File(widelandsRootFolder, "data") }

    override val pathToResource : String get() = widelandsDataFolder.absolutePath
    override val useGyroscope = false
    override val mouseButtonsEventsCanBeInvoked = true
    override val touchFullScreenModeCanBeUsed = false
    override val gl4esShaderCacheFolderName = "widelands_gl4es_cache"
    override val engineReadyToStart: Boolean get() = widelandsDataFolder.exists() &&
            preferencesStorage.widelandsFilesContentDownloaded.value!!
    override val commandLineParams: String get() = preferencesStorage.widelandsCommandLineArgs.value!!

    private external fun setPathsToResources (pathToRootUserFolder : String, pathToDataFolder : String)
    private external fun set_screen_scale (screenScale : Float)
    private external fun set_screen_controls_state (isControlsActive : Boolean)

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(WidelandsEngineInfo::class.java, mainLibraryName)
        setPathsToResources(widelandsRootFolder.absolutePath, pathToResource)
        set_screen_scale(preferencesStorage.widelandsScreenScale.value!!)
        set_screen_controls_state(!preferencesStorage.hideScreenControls.value!!)
    }

    companion object{
        const val WIDELANDS_FILES_CONTENT_CURRENT_VERSION : Int = 1
    }
}