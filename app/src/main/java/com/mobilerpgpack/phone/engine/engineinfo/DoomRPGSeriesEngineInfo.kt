package com.mobilerpgpack.phone.engine.engineinfo

import android.app.Activity
import android.system.Os
import androidx.compose.runtime.Composable
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.translator.ITranslationManager
import com.mobilerpgpack.phone.ui.screen.screencontrols.DrawDoomRpgSeriesKeyboard
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.component.inject
import kotlin.getValue

open class DoomRPGSeriesEngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>,
    buttonsToDraw: Collection<IScreenControlsView>,
    activeEngineType: EngineTypes,
    pathToResourceFlow: Flow<String>) :
    SDL2EngineInfo(mainEngineLib, allLibs, buttonsToDraw, activeEngineType, pathToResourceFlow) {

    private val translationManager: ITranslationManager by inject()

    override suspend fun initialize(activity: Activity) {
        super.initialize(activity)

        val useSdlTTFForTextRendering = preferencesStorage.useSDLTTFForFontsRendering.first()
        val enableMachineTranslation = preferencesStorage.enableGameMachineTextTranslation.first()

        Os.setenv("ENABLE_SDL_TTF", useSdlTTFForTextRendering.toString().lowercase(), true)
        Os.setenv(
            "ENABLE_TEXTS_MACHINE_TRANSLATION",
            enableMachineTranslation.toString().lowercase(), true
        )

        translationManager.inGame = true
        translationManager.activeEngine = engineType
    }

    @Composable
    override fun DrawVirtualKeyboard() {
        super.DrawVirtualKeyboard()
        DrawDoomRpgSeriesKeyboard()
    }

}

