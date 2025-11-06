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

class WolfensteinRpgEngineInfo(private val mainEngineLib: String,
    private val allLibs: Array<String>,
    private val buttonsToDraw: Collection<IScreenControlsView>) :
    DoomRPGSeriesEngineInfo(mainEngineLib, allLibs, buttonsToDraw) {

    override val pathToResource: Flow<String> = preferencesStorage.pathToWolfensteinRpgIpaFile

    override val engineType: EngineTypes = EngineTypes.WolfensteinRpg

    @Composable
    override fun DrawSettings() {
        val context = LocalContext.current
        val previousPathToWolfensteinRpgIPa by preferencesStorage.pathToWolfensteinRpgIpaFile
            .collectAsState(initial = "")

        RequestPath(
            context.getString(R.string.wolfenstein_rpg_ipa_file), onPathSelected = { selectedPath ->
                scope.launch { preferencesStorage.setPathToWolfensteinRpgIpaFile( selectedPath) }
            },
            previousPathToWolfensteinRpgIPa
        )
        HorizontalDivider()
        super.DrawSettings()
    }
}