package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

class DoomRpgComposeSettings : CommonDoomRpgComposeSettings() {

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val engineInfo : IEngineInfo = koinInject (named(EngineTypes.DoomRpg.name))
        val preferencesStorage : PreferencesStorage = koinInject()
        val savedPathToDoomRpgZip = preferencesStorage.pathToDoomRpgZipFile
        RequestPath(
            stringResource(R.string.doom_rpg_zip_file),
            savedPathToDoomRpgZip,
            requestMode = RequestPathMode.File,
            requiredFileExtensions = engineInfo.requiredResourceExtensions){ selectedPath ->
            preferencesStorage.setPathToDoomRpgZipFile(selectedPath)
        }
        DrawHorizontalDivider()
        super.DrawSettings(navController)
    }
}