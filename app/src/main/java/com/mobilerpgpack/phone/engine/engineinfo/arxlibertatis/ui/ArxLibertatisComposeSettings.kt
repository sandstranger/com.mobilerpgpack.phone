package com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis.ArxLibertatisPreferenceStorage
import com.mobilerpgpack.phone.ui.items.EditTextItem
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class ArxLibertatisComposeSettings : IEngineUIController {

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val viewModel : ArxLibertatisComposeSettingsViewModel = koinViewModel ()
        val preferencesStorage : ArxLibertatisPreferenceStorage = koinInject()

        val showContent by rememberSaveable(viewModel.showView) {
            mutableStateOf(viewModel.showView) }

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
                viewModel.textLocalization
            ) {
                viewModel.textLocalization = it
            }

            DrawHorizontalDivider()

            ListPreferenceItem(stringResource(R.string.audio_localization),
                viewModel.audioLocalization) {
                viewModel.audioLocalization = it
            }

            DrawHorizontalDivider()

            EditTextItem(stringResource(R.string.font_size),
                viewModel.fontSize) {
                viewModel.fontSize = it
            }

            DrawHorizontalDivider()

            EditTextItem(stringResource(R.string.hud_scale),
                viewModel.hudScale) {
                viewModel.hudScale = it
            }

            DrawHorizontalDivider()

            EditTextItem(stringResource(R.string.cursor_scale),
                viewModel.cursorScale) {
                viewModel.cursorScale = it
            }
        }
    }
}