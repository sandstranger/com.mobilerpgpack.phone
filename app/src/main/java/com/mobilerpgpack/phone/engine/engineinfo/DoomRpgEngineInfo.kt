package com.mobilerpgpack.phone.engine.engineinfo

import android.app.Activity
import android.system.Os
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.items.RequestPath
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.libsdl.app.SDLSurface

class DoomRpgEngineInfo(
    private val mainEngineLib: String,
    private val allLibs: Array<String>,
    private val buttonsToDraw: Collection<ButtonState>,
) : DoomRPGSeriesEngineInfo(mainEngineLib, allLibs, buttonsToDraw, EngineTypes.DoomRpg) {

    private var savedDoomRpgScreenWidth: Int = 0
    private var savedDoomRpgScreenHeight: Int = 0

    override val pathToResource: Flow<String> = super.preferencesStorage.pathToDoomRpgZipFile

    override suspend fun initialize(activity: Activity) {
        super.initialize(activity)
        recalculateGameScreenResolution()
    }

    @Composable
    override fun DrawSettings() {
        val context = LocalContext.current

        val savedPathToDoomRpgZip by preferencesStorage.pathToDoomRpgZipFile
            .collectAsState(initial = "")

        RequestPath(
            context.getString(R.string.doom_rpg_zip_file),
            onPathSelected = { selectedPath ->
                scope.launch { preferencesStorage.setPathToDoomRpgZipFile( selectedPath) }
            },
            savedPathToDoomRpgZip,
        )
        HorizontalDivider()
        super.DrawSettings()
    }

    private suspend fun recalculateGameScreenResolution() {
        val (width, height) = getDefaultDoomRpgResolution()

        savedDoomRpgScreenWidth = preferencesStorage.getIntValue(preferencesStorage.savedDoomRpgScreenWidthPrefsKey).first()
        savedDoomRpgScreenHeight= preferencesStorage.getIntValue(preferencesStorage.savedDoomRpgScreenHeightPrefsKey).first()

        if (savedDoomRpgScreenWidth != width && savedDoomRpgScreenHeight != height) {
            scope.launch {
                preferencesStorage.setIntValue(preferencesStorage.savedDoomRpgScreenWidthPrefsKey, width)
                preferencesStorage.setIntValue(preferencesStorage.savedDoomRpgScreenHeightPrefsKey, height)
            }

            Os.setenv("RECALCULATE_RESOLUTION_INDEX", "true", true)
        } else {
            Os.setenv("RECALCULATE_RESOLUTION_INDEX", "false", true)
        }
        Os.setenv("SCREEN_WIDTH", width.toString(), true)
        Os.setenv("SCREEN_HEIGHT", height.toString(), true)
        Os.setenv("FORCE_FILE_PATH", "true", true)
    }

    private fun getDefaultDoomRpgResolution(): Pair<Int, Int> {
        if (SDLSurface.fixedWidth > 0 && SDLSurface.fixedHeight > 0) {
            return SDLSurface.fixedWidth to SDLSurface.fixedHeight
        }

        return resolution
    }
}