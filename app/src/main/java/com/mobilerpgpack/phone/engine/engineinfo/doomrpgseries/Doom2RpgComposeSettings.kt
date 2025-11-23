package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.getValue

class Doom2RpgComposeSettings(buttonsToDraw: Collection<IScreenControlsView>) :
    CommonDoomRpgComposeSettings(buttonsToDraw) {

    private val engineInfo : IEngineInfo by inject (named(EngineTypes.Doom2Rpg.toString()))

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val previousPathToDoom2RpgIpa by preferencesStorage.pathToDoom2RpgIpaFile
            .collectAsState(initial = "")

        RequestPath(
            stringResource(R.string.doom2_rpg_ipa_file),
            previousPathToDoom2RpgIpa,
            requestMode = RequestPathMode.File,
            requiredFileExtensions = arrayListOf(engineInfo.requiredResourceExtension)){ selectedPath ->
            scope.launch { preferencesStorage.setPathToDoom2RpgIpaFile(selectedPath) }
        }

        HorizontalDivider()

        super.DrawSettings(navController)
    }
}