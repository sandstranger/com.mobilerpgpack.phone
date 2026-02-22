package com.mobilerpgpack.phone.engine.engineinfo.openxray

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.sun.jna.Native
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class OpenXrayEngineInfo (mainEngineLib: String,
                          allLibs: Array<String>) :
    SDL2EngineInfo(mainEngineLib, allLibs, EngineTypes.OpenXRAY){

    private val prefsStorage : OpenXrayPreferencesStorage by inject (
        named(EngineTypes.OpenXRAY.name))

    private val activeGame get() = prefsStorage.activeOpenXrayGame.value!!

    override val preferencesStorage: PreferencesStorage get() = prefsStorage

    override val commandLineParams get() = prefsStorage.openXrayCommandLineArgs.value!!

    override val loadGL4ES = true

    override val enableNGGL4ESSimpleShaderConv = false

    override val mouseButtonsEventsCanBeInvoked = true

    override val pathToResource: String
        get() {
            return when (activeGame) {
                OpenXrayGames.ClearSky -> prefsStorage.pathToClearSkyResources.value!!
                OpenXrayGames.CallOfPripyat -> prefsStorage.pathToCallOfPripyatResources.value!!
            }
        }

    override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs

            return mutableListOf<String>().let {
                it += baseCommandLineArgs

                if (!baseCommandLineArgs.contains(CLEAR_SKY_COMMAND) &&
                    !baseCommandLineArgs.contains(CALL_OF_PRIPYAT_COMMAND)) {
                    it += when (prefsStorage.activeOpenXrayGame.value!!) {
                        OpenXrayGames.ClearSky -> CLEAR_SKY_COMMAND
                        OpenXrayGames.CallOfPripyat -> CALL_OF_PRIPYAT_COMMAND
                    }
                }

                it.toTypedArray()
            }
        }

    private external fun setPathToResources (pathToDataFolder : String)

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(OpenXrayEngineInfo::class.java, mainLibraryName)
        setPathToResources(pathToResource)
    }

    private companion object{
        private const val CALL_OF_PRIPYAT_COMMAND = "-cop"
        private const val CLEAR_SKY_COMMAND = "-cs"
    }
}