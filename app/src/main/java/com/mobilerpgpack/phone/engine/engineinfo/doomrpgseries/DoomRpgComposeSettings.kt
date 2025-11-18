package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import kotlinx.coroutines.launch

class DoomRpgComposeSettings (buttonsToDraw: Collection<IScreenControlsView>) :
    CommonDoomRpgComposeSettings(buttonsToDraw) {

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val savedPathToDoomRpgZip by preferencesStorage.pathToDoomRpgZipFile
            .collectAsState(initial = "")

        RequestPath(
            stringResource(R.string.doom_rpg_zip_file),
            savedPathToDoomRpgZip,
            requestMode = RequestPathMode.Archive){ selectedPath ->
            scope.launch { preferencesStorage.setPathToDoomRpgZipFile(selectedPath) }
        }
        HorizontalDivider()
        super.DrawSettings(navController)
    }
}