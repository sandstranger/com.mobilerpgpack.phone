package com.mobilerpgpack.phone.engine.engineinfo

import android.app.Activity
import android.system.Os
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.component.inject

open class DoomRPGSeriesEngineInfo(private val mainEngineLib: String,
                                   private val allLibs : Array<String>,
                                   private val buttonsToDraw : Collection<ButtonState>,
                                   private val activeEngineType : EngineTypes,
                                   private val pathToResourceFlow : Flow<String?>
) : SDL2EngineInfo(mainEngineLib, allLibs, buttonsToDraw, activeEngineType,pathToResourceFlow) {

    private val translationManager : TranslationManager by inject ()

    override suspend fun initialize(activity: Activity) {
        super.initialize(activity)

        val useSdlTTFForTextRendering = preferencesStorage.useSDLTTFForFontsRendering.first()
        val enableMachineTranslation = preferencesStorage.enableGameMachineTextTranslation.first()

        Os.setenv("ENABLE_SDL_TTF", useSdlTTFForTextRendering.toString().lowercase(),true)
        Os.setenv("ENABLE_TEXTS_MACHINE_TRANSLATION",
            enableMachineTranslation.toString().lowercase(),true)

        translationManager.inGame = true
        translationManager.activeEngine = engineType


    }
}