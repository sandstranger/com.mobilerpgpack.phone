package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.sdl.SDL3EngineInfo
import com.mobilerpgpack.phone.translator.ITranslationManager
import com.quantuminventions.customkeyboard.components.keyboard.CustomKeyboardView
import com.sun.jna.Native
import org.koin.core.component.inject

abstract class DoomRPGSeriesEngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>,
    activeEngineType: EngineTypes) :
    SDL3EngineInfo(mainEngineLib, allLibs, activeEngineType) {
    private val translationManager: ITranslationManager by inject()
    final override val mouseButtonsEventsCanBeInvoked: Boolean = true
    override val requiredResourceExtensions = listOf(".ipa", ".IPA")
    final override val touchFullScreenModeCanBeUsed: Boolean = false
    final override val keyboardInputType = CustomKeyboardView.KeyboardType.NUMBER_DECIMAL
    final override val enableGyroscope = false
    final override val enableNGGL4ESSimpleShaderConv = true

    private external fun setEnableSDLTTFState (enableSDLTTF : Boolean)
    private external fun setEnableMachineTranslationState (enableMachineTranslation : Boolean)
    private external fun setPathsToResources (pathToArchive : String, pathToUserFolder : String)

    override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        translationManager.inGame = true
        translationManager.activeEngine = engineType
    }

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(DoomRPGSeriesEngineInfo::class.java, mainLibraryName)
        val useSdlTTFForTextRendering = preferencesStorage.useSDLTTFForFontsRendering.value!!
        val enableMachineTranslation = preferencesStorage.enableGameMachineTextTranslation.value!!
        setEnableSDLTTFState(useSdlTTFForTextRendering)
        setEnableMachineTranslationState(enableMachineTranslation)
        setPathsToResources(pathToResource, preferencesStorage.pathToRootUserFolder.value!!)
    }

    override fun isMouseShown() = true
}

