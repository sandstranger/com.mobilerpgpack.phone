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
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class DoomRpgComposeSettings : CommonDoomRpgComposeSettings() {

    private val engineInfo : IEngineInfo by inject (named(EngineTypes.DoomRpg.toString()))

    @Composable
    override fun DrawSettings(navController: NavHostController) {
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