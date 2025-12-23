package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class Doom2RpgComposeSettings : CommonDoomRpgComposeSettings() {

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val engineInfo : IEngineInfo = koinInject (named(EngineTypes.Doom2Rpg.name))
        val preferencesStorage : PreferencesStorage = koinInject()
        val previousPathToDoom2RpgIpa = preferencesStorage.pathToDoom2RpgIpaFile
        RequestPath(
            stringResource(R.string.doom2_rpg_ipa_file),
            previousPathToDoom2RpgIpa,
            requestMode = RequestPathMode.File,
            requiredFileExtensions = engineInfo.requiredResourceExtensions){ selectedPath ->
            preferencesStorage.setPathToDoom2RpgIpaFile(selectedPath)
        }

        DrawHorizontalDivider()

        super.DrawSettings(navController)
    }
}