package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.getValue
import androidx.compose.ui.res.stringResource
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider

class WolfensteinRpgComposeSettings : CommonDoomRpgComposeSettings(){

    private val engineInfo : IEngineInfo by inject (named(EngineTypes.WolfensteinRpg.toString()))

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val previousPathToWolfensteinRpgIPa = preferencesStorage.pathToWolfensteinRpgIpaFile
        RequestPath(
            stringResource(R.string.wolfenstein_rpg_ipa_file),
            previousPathToWolfensteinRpgIPa,
            requestMode = RequestPathMode.File,
            requiredFileExtensions = engineInfo.requiredResourceExtensions){ selectedPath ->
            preferencesStorage.setPathToWolfensteinRpgIpaFile(selectedPath)
        }
        DrawHorizontalDivider()
        super.DrawSettings(navController)
    }
}