package com.mobilerpgpack.phone.engine.engineinfo

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.items.RequestPath
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class Doom2RpgEngineInfo(
    private val mainEngineLib: String,
    private val allLibs: Array<String>,
    private val buttonsToDraw: Collection<IScreenControlsView>) :
    DoomRPGSeriesEngineInfo(mainEngineLib, allLibs, buttonsToDraw, EngineTypes.Doom2Rpg) {

    override val pathToResource: Flow<String> = preferencesStorage.pathToDoom2RpgIpaFile

    @Composable
    override fun DrawSettings() {
        val context = LocalContext.current
        val previousPathToDoom2RpgIpa by preferencesStorage.pathToDoom2RpgIpaFile
            .collectAsState(initial = "")

        RequestPath(
            context.getString(R.string.doom2_rpg_ipa_file),
            onPathSelected = { selectedPath ->
                scope.launch { preferencesStorage.setPathToDoom2RpgIpaFile( selectedPath) }
            },
            previousPathToDoom2RpgIpa
        )

        HorizontalDivider()

        super.DrawSettings()
    }
}