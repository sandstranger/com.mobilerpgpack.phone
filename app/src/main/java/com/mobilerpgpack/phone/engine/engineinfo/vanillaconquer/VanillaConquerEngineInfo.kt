package com.mobilerpgpack.phone.engine.engineinfo.vanillaconquer

import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.mobilerpgpack.phone.main.RED_ALERT_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.TIBERIAN_DAWN_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.sun.jna.Native
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File

class VanillaConquerEngineInfo :
    SDL3EngineInfo("", emptyArray(), EngineTypes.VanillaConquer) {

    private val configsDirectory : File by inject { parametersOf("vanilla-conquer") }

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

    override val allowedToEnableAngle = false

    override val touchFullScreenModeCanBeUsed = false

    override val commandLineParams get() = prefsStorage.vanillaConquerCommandLineArgs.value!!

    override val pathToResource: String
        get() = when (activeGame) {
            VanillaConquerGames.TiberianDawn -> prefsStorage.pathToTiberianDawnResources.value!!
            VanillaConquerGames.RedAlert -> prefsStorage.pathToRedAlertResources.value!!
        }

    override val nativeLibraries: Array<String> get() =
        mutableListOf<String>().run {
            this += super.nativeLibraries
            this += when (activeGame) {
                VanillaConquerGames.TiberianDawn -> tiberianDawnNativeLibs
                VanillaConquerGames.RedAlert -> redAlertNativeLibs
            }
            toTypedArray()
        }

    override val mainLibraryName: String
        get() = when (activeGame) {
            VanillaConquerGames.TiberianDawn -> TIBERIAN_DAWN_NATIVE_LIB_NAME
            VanillaConquerGames.RedAlert -> RED_ALERT_NATIVE_LIB_NAME
        }

    override val preferencesStorage: PreferencesStorage get() = prefsStorage

    private external fun setPathToConfigsDirectory (pathToConfigsDirectory : String)

    private external fun setPathToResources (pathToResource : String)

    private external fun setEnableVsyncState (enableVsync : Boolean)

    private external fun setUseDoseModeState (useDosMode : Boolean)

    private external fun setFrameRateLimit (frameRateLimit : Int)

    private external fun setOnScreenControlsState (onscreenControlsActive : Boolean)

    private external fun setMouseSensitivity (mouseSensitivity : Int)

    private external fun setControllerPointerSpeed (controllerPointerSpeed : Int)

    override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        configsDirectory.run {
            mkdirs()
            File (this, "vanillatd").apply { mkdirs() }
            File(this, "vanillara").mkdirs()
        }
    }

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(VanillaConquerEngineInfo::class.java, mainLibraryName)
        setPathToResources(pathToResource)
        setPathToConfigsDirectory(configsDirectory.absolutePath)
        prefsStorage.apply {
            setEnableVsyncState(vanillaConquerEnableVsync.value!!)
            setUseDoseModeState(enableDosMode.value!!)
            setFrameRateLimit(vanillaConquerFrameRateLimit.value!!)
            setOnScreenControlsState(!preferencesStorage.hideScreenControls.value!!)
            setMouseSensitivity(vanillaConquerMouseSensitivity.value!!)
            setControllerPointerSpeed(vanillaConquerControllerPointerSpeed.value!!)
        }
    }
}