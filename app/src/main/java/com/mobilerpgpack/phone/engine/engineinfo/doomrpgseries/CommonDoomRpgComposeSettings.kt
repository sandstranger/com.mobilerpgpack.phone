package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import android.util.Log
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.translator.ITranslationManager
import com.mobilerpgpack.phone.translator.ITranslationModelsDownloader
import com.mobilerpgpack.phone.translator.models.TranslationType
import com.mobilerpgpack.phone.ui.items.DrawTitleText
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.PreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.screen.LoadingModelDialogWithCancel
import com.mobilerpgpack.phone.ui.screen.utils.buildTranslationsDescription
import com.mobilerpgpack.phone.ui.screen.viewmodels.DownloadViewModel
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

open class CommonDoomRpgComposeSettings :
    KoinComponent, IEngineUIController {

    private val translationModelsDownloader : ITranslationModelsDownloader by inject ()

    private val translationManager : ITranslationManager by inject()

    protected val scope : CoroutineScope by inject (named(KoinModulesProvider.COROUTINES_SCOPE))

    protected val preferencesStorage : PreferencesStorage by inject()

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        DrawTranslationModelSettings()
    }

    @Composable
    private fun DrawTranslationModelSettings() {
        val activeTranslationTypeString = preferencesStorage.translationModelType
        val configuration = LocalConfiguration.current
        val translationModelEntries = buildTranslationsDescription()
        var initialModelValue by rememberSaveable { mutableStateOf("") }
        val isModelDownloaded by translationManager.isTranslationSupportedAsFlow().collectAsState(initial = true)

        LaunchedEffect( configuration.locales[0],Unit) {
            initialModelValue = translationModelEntries.first { it.startsWith(activeTranslationTypeString) }
        }

        LaunchedEffect(isModelDownloaded) {
            if (!isModelDownloaded) {
                preferencesStorage.setEnableGameMachineTextTranslationValue(false)
            }
        }

        DrawTitleText(stringResource(R.string.translation_settings))

        if (initialModelValue.isNotEmpty() && translationModelEntries.isNotEmpty()) {
            ListPreferenceItem(
                stringResource(R.string.translation_model_title),
                initialModelValue,
                translationModelEntries, updateForced = true
            ) { newValue ->
                with(TranslationType.getTranslationType(newValue)){
                    translationManager.activeTranslationType = this
                    preferencesStorage.setTranslationModelTypeValue(this)
                    initialModelValue = newValue
                }
            }

            DrawHorizontalDivider()
        }

        SwitchPreferenceItem(
            stringResource(R.string.allow_downloading_over_mobile_network),
            preferencesStorage.allowDownloadingModelsOverMobile,
            preferencesStorage.allowDownloadingModelsOverMobilePrefsKey.name
        ) {
            translationModelsDownloader.allowDownloadingOveMobile = it
        }

        DrawHorizontalDivider()

        DrawPreloadModelsSetting()

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.use_sdl_ttf_for_rendering),
            preferencesStorage.useSDLTTFForFontsRendering,
            preferencesStorage.useSDLTTFForFontsRenderingPrefsKey.name
        )

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.use_ai_for_text_translations),
            preferencesStorage.enableGameMachineTextTranslation,
            preferencesStorage.gamesMachineTranslationsPrefsKey.name,
            enabled = isModelDownloaded
        )
    }

    @Composable
    private fun DrawPreloadModelsSetting(vm: DownloadViewModel = koinViewModel()) {
        val activeTranslationTypeString = preferencesStorage.translationModelType
        LaunchedEffect(activeTranslationTypeString) {
            if (activeTranslationTypeString != "") {
                vm.onTranslationTypeChanged(activeTranslationTypeString)
            }
        }

        PreferenceItem(stringResource(R.string.load_translation_model)) {
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