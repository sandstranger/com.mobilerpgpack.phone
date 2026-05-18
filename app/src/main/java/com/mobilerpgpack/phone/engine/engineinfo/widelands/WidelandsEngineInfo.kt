package com.mobilerpgpack.phone.engine.engineinfo.widelands

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.sun.jna.Native
import java.io.File

class WidelandsEngineInfo (mainEngineLib: String,
                           allLibs: Array<String>) :
    SDL3EngineInfo(mainEngineLib, allLibs, EngineTypes.Widelands) {

    private val pathToWidelandsRootFolder get() =
        preferencesStorage.pathToRootUserFolder.value!! + File.separator + "widelands"

    override val pathToResource get() = pathToWidelandsRootFolder + File.separator + "data"

    override val useGyroscope = false

    override val mouseButtonsEventsCanBeInvoked = true

    override val touchFullScreenModeCanBeUsed = false

    override val commandLineParams: String get() = preferencesStorage.widelandsCommandLineArgs.value!!

    private external fun setPathsToResources (pathToRootUserFolder : String, pathToDataFolder : String)

    private external fun set_screen_scale (screenScale : Float)

    private external fun set_screen_controls_state (isControlsActive : Boolean)

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(WidelandsEngineInfo::class.java, mainLibraryName)
        setPathsToResources(pathToWidelandsRootFolder, pathToResource)
        set_screen_scale(preferencesStorage.widelandsScreenScale.value!!)
        set_screen_controls_state(!preferencesStorage.hideScreenControls.value!!)
    }
}