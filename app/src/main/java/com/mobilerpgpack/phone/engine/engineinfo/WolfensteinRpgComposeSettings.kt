package com.mobilerpgpack.phone.engine.engineinfo

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.ui.items.RequestPath
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import kotlinx.coroutines.launch

class WolfensteinRpgComposeSettings (private val buttonsToDraw: Collection<IScreenControlsView>) :
    CommonDoomRpgComposeSettings (buttonsToDraw){

    @Composable
    override fun DrawSettings() {
        val context = LocalContext.current
        val previousPathToWolfensteinRpgIPa by preferencesStorage.pathToWolfensteinRpgIpaFile
            .collectAsState(initial = "")

        RequestPath(
            context.getString(R.string.wolfenstein_rpg_ipa_file), onPathSelected = { selectedPath ->
                scope.launch { preferencesStorage.setPathToWolfensteinRpgIpaFile(selectedPath) }
            },
            previousPathToWolfensteinRpgIPa
        )
        HorizontalDivider()
        super.DrawSettings()
    }
}