package com.mobilerpgpack.phone.engine.engineinfo

import android.app.Activity
import android.system.Os
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.translator.TranslationManager
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.ui.items.DrawTitleText
import com.mobilerpgpack.phone.ui.items.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.PreferenceItem
import com.mobilerpgpack.phone.ui.items.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.screen.LoadingModelDialogWithCancel
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState
import com.mobilerpgpack.phone.ui.screen.utils.buildTranslationsDescription
import com.mobilerpgpack.phone.ui.screen.viewmodels.DownloadViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.inject
import java.io.File
import kotlin.collections.first

abstract class DoomRPGSeriesEngineInfo(private val mainEngineLib: String,
                                   private val allLibs : Array<String>,
                                   private val buttonsToDraw : Collection<ButtonState>,
                                   private val activeEngineType : EngineTypes) :
    SDL2EngineInfo(mainEngineLib, allLibs, buttonsToDraw, activeEngineType) {

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

    @Composable
    override fun DrawSettings() {
        DrawTranslationModelSettings()
    }

    @Composable
    private fun DrawTranslationModelSettings() {
        val scope= rememberCoroutineScope ()
        val context = LocalContext.current
        DrawTitleText(context.getString(R.string.translation_settings))

        val activeTranslationTypeString by preferencesStorage.translationModelType
            .collectAsState(initial = TranslationType.DefaultTranslationType.toString())

        val translationModelEntries = buildTranslationsDescription()
        val initialModelValue = translationModelEntries.first { it.startsWith(activeTranslationTypeString) }

        ListPreferenceItem(
            context.getString(R.string.translation_model_title),
            initialModelValue,
            translationModelEntries
        ) { newValue ->
            scope.launch {
                val translationModelType = TranslationType.getTranslationType(newValue)
                translationManager.activeTranslationType = translationModelType
                preferencesStorage.setTranslationModelTypeValue( translationModelType)
            }
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.allow_downloading_over_mobile_network),
            preferencesStorage.allowDownloadingModelsOverMobile,
            preferencesStorage.allowDownloadingModelsOverMobilePrefsKey.name)

        HorizontalDivider()

        DrawPreloadModelsSetting()
    }

    @Composable
    private fun DrawPreloadModelsSetting(vm: DownloadViewModel = koinViewModel()) {
        val context = LocalContext.current
        val activeTranslationTypeString by preferencesStorage.translationModelType
            .collectAsState(initial = "")

        LaunchedEffect(activeTranslationTypeString) {
            if (activeTranslationTypeString != "") {
                vm.onTranslationTypeChanged(activeTranslationTypeString)
            }
        }

        PreferenceItem(context.getString(R.string.load_translation_model)) {
            vm.startDownload()
        }

        LoadingModelDialogWithCancel(
            show = vm.isLoading,
            progress = vm.downloadProgress,
            onClose = {
                vm.isLoading = false
            },
            onCancel = {
                vm.cancelDownload()
            }
        )
    }
}