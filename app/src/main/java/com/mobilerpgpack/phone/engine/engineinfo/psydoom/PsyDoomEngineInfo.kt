package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.main.FREETYPE_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.PSYDOOM_MAIN_ENGINE_LIB
import com.mobilerpgpack.phone.main.SDL2_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.screencontrols.wolfensteinButtons
import com.sun.jna.Function
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.util.Collections.addAll

class PsyDoomEngineInfo(mainEngineLib: String,
                        allLibs: Array<String>,
                        buttonsToDraw: Collection<IScreenControlsView>,
                        commandLineParamsFlow : Flow<String>) :
    SDL2EngineInfo (mainEngineLib, allLibs, buttonsToDraw,activeEngineType = EngineTypes.PsyDoom,
        commandLineParamsFlow = commandLineParamsFlow) {

    private val psyDoomPreferencesStorage by inject <PsyDoomPreferencesStorage>(named(
        EngineTypes.PsyDoom.toString()))

    private val onApplicationPauseNativeDelegate by lazy {
        Function.getFunction(mainEngineLib,
            "onApplicationPause")
    }

    private val onApplicationResumeNativeDelegate by lazy {
        Function.getFunction(mainEngineLib,
            "onApplicationResume")
    }

    override val preferencesStorage = psyDoomPreferencesStorage

    override val pathToResource get() = psyDoomPreferencesStorage.pathToPsyDoomCueFile

    override val needToShowScreenControls = true

    override val mouseButtonsEventsCanBeInvoked = false

    override fun isMouseShown() = false

    override val commandLineArgs: Array<String>
        get() {
            val baseCommandLineArgs = super.commandLineArgs

            if (baseCommandLineArgs.contains(CUE_COMMAND)){
                return baseCommandLineArgs
            }

            return mutableListOf<String>().let {
                it.addAll(baseCommandLineArgs)

                runBlocking {
                    it += CUE_COMMAND
                    it += psyDoomPreferencesStorage.pathToPsyDoomCueFile.first()

                    val modsFolder = psyDoomPreferencesStorage.pathToPsyDoomModsFolder.first()

                    if (modsFolder.isNotEmpty()){
                        it += DATA_DIR_COMMAND
                        it += modsFolder
                    }
                }

                it.toTypedArray()
            }
        }

    override fun onResume() = onApplicationResumeNativeDelegate.invokeVoid(null)

    override fun onDestroy() = onApplicationPauseNativeDelegate.invokeVoid(null)

    private companion object{
        private const val CUE_COMMAND = "-cue"
        private const val DATA_DIR_COMMAND = "-datadir"
    }
}