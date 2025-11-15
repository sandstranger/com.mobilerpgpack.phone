package com.mobilerpgpack.phone.engine.engineinfo

import android.system.Os
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.translator.ITranslationManager
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.utils.invokeBool
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.component.inject

open class DoomRPGSeriesEngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>,
    buttonsToDraw: Collection<IScreenControlsView>,
    activeEngineType: EngineTypes,
    pathToResourceFlow: Flow<String>) :
    SDL2EngineInfo(mainEngineLib, allLibs, buttonsToDraw, activeEngineType, pathToResourceFlow) {

    private val translationManager: ITranslationManager by inject()

    override fun isMouseShown() = true

    override suspend fun initialize(activity: ComponentActivity) {
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
}

