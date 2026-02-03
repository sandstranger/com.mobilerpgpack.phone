package com.mobilerpgpack.phone.engine.engineinfo.vanillaconquer

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.main.RED_ALERT_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.TIBERIAN_DAWN_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.sun.jna.Native
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class VanillaConquerEngineInfo :
    SDL2EngineInfo ("", emptyArray(), EngineTypes.VanillaConquer) {
    private val prefsStorage : VanillaConquerPreferencesStorage by inject (
        named(EngineTypes.VanillaConquer.name))

    private val activeGame get() = prefsStorage.activeVanillaConquerGame.value!!

    private val redAlertNativeLibs : Array<String> by inject (
        named(VanillaConquerGames.RedAlert.name))

    private val tiberianDawnNativeLibs : Array<String> by inject (
        named(VanillaConquerGames.TiberianDawn.name))

    override val mouseButtonsEventsCanBeInvoked = true

    override val loadGL4ES = false

    override val enableGyroscope = false

    override val touchFullScreenModeCanBeUsed = false

    override val pathToResource: String
        get() = when (activeGame) {
            VanillaConquerGames.TiberianDawn -> prefsStorage.pathToTiberianDawnResources.value!!
            VanillaConquerGames.RedAlert -> prefsStorage.pathToRedAlertResources.value!!
        }

    override val nativeLibraries: Array<String>
        get() = when (activeGame) {
            VanillaConquerGames.TiberianDawn -> tiberianDawnNativeLibs
            VanillaConquerGames.RedAlert -> redAlertNativeLibs
        }

    override val mainLibraryName: String
        get() = when (activeGame) {
            VanillaConquerGames.TiberianDawn -> TIBERIAN_DAWN_NATIVE_LIB_NAME
            VanillaConquerGames.RedAlert -> RED_ALERT_NATIVE_LIB_NAME
        }

    override val preferencesStorage: PreferencesStorage get() = prefsStorage

    private external fun setPathToResources (pathToResource : String)

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(VanillaConquerEngineInfo::class.java, mainLibraryName)
        setPathToResources(pathToResource)
    }
}