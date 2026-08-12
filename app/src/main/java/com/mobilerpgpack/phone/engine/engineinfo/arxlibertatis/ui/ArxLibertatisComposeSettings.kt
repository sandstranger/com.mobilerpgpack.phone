package com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.preference.SwitchPreference
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis.ArxLibertatisLocalizationType
import com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis.ArxLibertatisPreferenceStorage
import com.mobilerpgpack.phone.ui.items.EditTextItem
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class ArxLibertatisComposeSettings : IEngineUIController {

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val viewModel : ArxLibertatisComposeSettingsViewModel = koinViewModel ()
        val preferencesStorage : ArxLibertatisPreferenceStorage = koinInject()
        val showContent = viewModel.showView.getComposableValue()
        val localizationEntries = retain { ArxLibertatisLocalizationType.stringEntries }

        if (showContent){
            DrawCommandLinePreferences(preferencesStorage.arxLibertatisCommandLineArgs,
                preferencesStorage.arxLibertatisCommandLineArgsPrefsKey.name)

            DrawHorizontalDivider()

            RequestPath(
                stringResource(R.string.path_to_arx_fatalis_resources),
                preferencesStorage.pathToArxFatalisFolder,
                key = preferencesStorage.pathToArxFatalisFolderPrefsKey, requestMode = RequestPathMode.Directory)

            DrawHorizontalDivider()

            ListPreferenceItem(
                stringResource(R.string.text_localization),
                viewModel.textLocalizationAsLiveData,localizationEntries
            ) {
                viewModel.textLocalization = enumValueOf<ArxLibertatisLocalizationType>(it)
            }

            DrawHorizontalDivider()

            ListPreferenceItem(stringResource(R.string.audio_localization),
                viewModel.audioLocalizationAsLiveData,localizationEntries) {
                viewModel.audioLocalization = enumValueOf<ArxLibertatisLocalizationType>(it)
            }

            DrawHorizontalDivider()

            EditTextItem(stringResource(R.string.font_size),
                viewModel.fontSizeAsLiveData) {
                viewModel.fontSize = it
            }

            DrawHorizontalDivider()

            EditTextItem(stringResource(R.string.hud_scale),
                viewModel.hudScaleAsLiveData) {
                viewModel.hudScale = it
            }

            DrawHorizontalDivider()

            EditTextItem(stringResource(R.string.cursor_scale),
                viewModel.cursorScaleAsLiveData) {
                viewModel.cursorScale = it
            }

            DrawHorizontalDivider()

            SwitchPreferenceItem(stringResource(R.string.enable_etc2_texture_support),
                preferencesStorage.enableEtc2TextureSupport,
                preferencesStorage.enableEtc2TextureSupportPrefsKey.name)
        }
    }
}