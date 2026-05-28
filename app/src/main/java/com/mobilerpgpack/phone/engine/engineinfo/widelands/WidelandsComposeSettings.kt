package com.mobilerpgpack.phone.engine.engineinfo.widelands

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.items.EditTextItem
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.items.prefsitems.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.PreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.screen.LoadingModelDialogWithCancel
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getComposableValue
import com.mobilerpgpack.phone.utils.sharesprefs.stringPreferencesKey
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class WidelandsComposeSettings : IEngineUIController {
    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val preferencesStorage = koinInject<PreferencesStorage>()
        val vm : WidelandsViewModel= koinViewModel ()
        preferencesStorage.apply {
            DrawCommandLinePreferences(widelandsCommandLineArgs,
                widelandsCommandLineArgsPrefsKey.name)
            DrawHorizontalDivider()
            SwitchPreferenceItem(stringResource(R.string.allow_downloading_over_mobile_network),
                allowWidelandsDownloadsOverMobile,
                allowWidelandsDownloadsOverMobileNetworkPrefsKey.name)
            DrawHorizontalDivider()
            PreferenceItem(stringResource(R.string.download_widelands_resources)){
                vm.startDownload()
            }
            DrawHorizontalDivider()
            EditTextPreferenceItem(stringResource(R.string.screen_scale),
                widelandsScreenScale){
                setFloatValue(widelandsScreenScalePrefeKey, it.coerceAtLeast(1.0f))
            }
        }
        LoadingModelDialogWithCancel(
            show = vm.isLoading.getComposableValue(),
            progress = vm.downloadProgress.getComposableValue(),
            onClose = {
                vm.isLoading.value = false
            },
            onCancel = {
                vm.cancelDownload()
            }
        )
    }
}