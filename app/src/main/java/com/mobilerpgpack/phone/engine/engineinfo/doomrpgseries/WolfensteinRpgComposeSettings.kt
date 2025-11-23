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
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.getValue

class WolfensteinRpgComposeSettings (buttonsToDraw: Collection<IScreenControlsView>) :
    CommonDoomRpgComposeSettings(buttonsToDraw){

    private val engineInfo : IEngineInfo by inject (named(EngineTypes.WolfensteinRpg.toString()))

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val context = LocalContext.current
        val previousPathToWolfensteinRpgIPa by preferencesStorage.pathToWolfensteinRpgIpaFile
            .collectAsState(initial = "")

        RequestPath(
            context.getString(R.string.wolfenstein_rpg_ipa_file),
            previousPathToWolfensteinRpgIPa,
            requestMode = RequestPathMode.File,
            requiredFileExtensions = arrayListOf(engineInfo.requiredResourceExtension)){ selectedPath ->
            scope.launch { preferencesStorage.setPathToWolfensteinRpgIpaFile(selectedPath) }
        }
        HorizontalDivider()
        super.DrawSettings(navController)
    }
}