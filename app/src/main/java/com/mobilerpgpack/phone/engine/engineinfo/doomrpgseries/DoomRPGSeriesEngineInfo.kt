package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import android.system.Os
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL2EngineInfo
import com.mobilerpgpack.phone.translator.ITranslationManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.component.inject

open class DoomRPGSeriesEngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>,
    activeEngineType: EngineTypes,
    pathToResourceFlow: Flow<String>) :
    SDL2EngineInfo(mainEngineLib, allLibs, activeEngineType, pathToResourceFlow) {

    private val translationManager: ITranslationManager by inject()

    final override val mouseButtonsEventsCanBeInvoked: Boolean = true

    override val requiredResourceExtensions = listOf(".ipa", ".IPA")

    final override val touchFullScreenModeCanBeUsed: Boolean = false

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

    override fun isMouseShown() = true
}

