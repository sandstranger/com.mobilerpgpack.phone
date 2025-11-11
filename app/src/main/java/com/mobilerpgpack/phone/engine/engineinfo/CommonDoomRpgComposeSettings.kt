package com.mobilerpgpack.phone.engine.engineinfo

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.translator.ITranslationManager
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.ui.items.DrawTitleText
import com.mobilerpgpack.phone.ui.items.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.PreferenceItem
import com.mobilerpgpack.phone.ui.items.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.screen.LoadingModelDialogWithCancel
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import com.mobilerpgpack.phone.ui.screen.utils.buildTranslationsDescription
import com.mobilerpgpack.phone.ui.screen.viewmodels.DownloadViewModel
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

open class CommonDoomRpgComposeSettings (buttonsToDraw: Collection<IScreenControlsView>) :
    KoinComponent, IEngineUIController{

    private val translationManager : ITranslationManager by inject()

    protected val scope : CoroutineScope by inject (named(KoinModulesProvider.COROUTINES_SCOPE))

    protected val preferencesStorage : PreferencesStorage by inject()

    override val screenViewsToDraw: Collection<IScreenControlsView> = buttonsToDraw

    @Composable
    override fun DrawSettings() {
        DrawTranslationModelSettings()
    }

    @Composable
    private fun DrawTranslationModelSettings() {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        val activeTranslationTypeString by preferencesStorage.translationModelType
            .collectAsState(initial = TranslationType.DefaultTranslationType.toString())

        val translationModelEntries = buildTranslationsDescription()
        val initialModelValue = translationModelEntries.first { it.startsWith(activeTranslationTypeString) }
        val isModelDownloaded by translationManager.isTranslationSupportedAsFlow().collectAsState(initial = true)

        DrawTitleText(context.getString(R.string.translation_settings))

        ListPreferenceItem(
            context.getString(R.string.translation_model_title),
            initialModelValue,
            translationModelEntries
        ) { newValue ->
            scope.launch {
                val translationModelType = TranslationType.getTranslationType(newValue)
                translationManager.activeTranslationType = translationModelType
                preferencesStorage.setTranslationModelTypeValue(translationModelType)
            }
        }

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.allow_downloading_over_mobile_network),
            preferencesStorage.allowDownloadingModelsOverMobile,
            preferencesStorage.allowDownloadingModelsOverMobilePrefsKey.name
        ) {
            translationManager.allowDownloadingOveMobile = it
        }

        HorizontalDivider()

        DrawPreloadModelsSetting()

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.use_sdl_ttf_for_rendering),
            preferencesStorage.useSDLTTFForFontsRendering,
            preferencesStorage.useSDLTTFForFontsRenderingPrefsKey.name
        )

        HorizontalDivider()

        SwitchPreferenceItem(
            context.getString(R.string.use_ai_for_text_translations),
            preferencesStorage.enableGameMachineTextTranslation,
            preferencesStorage.gamesMachineTranslationsPrefsKey.name,
            enabled = isModelDownloaded
        )
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