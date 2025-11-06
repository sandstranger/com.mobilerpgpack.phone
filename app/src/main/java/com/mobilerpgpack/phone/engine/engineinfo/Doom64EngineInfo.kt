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
import com.mobilerpgpack.phone.ui.items.SwitchPreferenceItem
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState
import com.mobilerpgpack.phone.ui.screen.screencontrols.IScreenControlsView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class Doom64EngineInfo(private val mainEngineLib: String,
    private val allLibs: Array<String>,
    private val buttonsToDraw: Collection<IScreenControlsView>) :
    SDL3EngineInfo(mainEngineLib, allLibs,buttonsToDraw) {

    override val pathToResource: Flow<String> = preferencesStorage.pathToDoom64MainWadsFolder

    override val engineType: EngineTypes = EngineTypes.Doom64ExPlus

    override suspend fun initialize(activity: Activity) {
        super.initialize(activity)

        val pathToDoom64ModsFolder = getPathToDoom64ModsFolder()

        Os.setenv("PATH_TO_DOOM64_MODS_FOLDER", pathToDoom64ModsFolder, true)
        Os.setenv("PATH_TO_DOOM_64_USER_FOLDER", getPathToDoom64UserFolder(), true)
    }

    @Composable
    override fun DrawSettings() {
        val context = LocalContext.current
        val previousPathToDoom64WadsFolder by preferencesStorage.pathToDoom64MainWadsFolder
            .collectAsState(initial = "")

        RequestPath(
            context.getString(R.string.path_to_doom64_folder),
            onPathSelected = { selectedPath ->
                scope.launch { preferencesStorage.setPathToDoom64MainWadsFolder( selectedPath) }
            },
            previousPathToDoom64WadsFolder, requestOnlyDirectory = true
        )

        HorizontalDivider()

        val enableDoom64ModsFlow = preferencesStorage.enableDoom64Mods
        val enableDoom64Mods by enableDoom64ModsFlow.collectAsState(initial = false)

        SwitchPreferenceItem(
            context.getString(R.string.enable_doom64_mods),
            initialValue = enableDoom64Mods,
            preferencesStorage.enableDoom64ModsPrefsKey.name)

        val previousPathToDoom64ModsFolder by preferencesStorage.pathToDoom64ModsFolder
            .collectAsState(initial = "")

        if (enableDoom64Mods) {
            HorizontalDivider()

            RequestPath(
                context.getString(R.string.path_to_doom64_mods_folder),
                onPathSelected = { selectedPath ->
                    scope.launch { preferencesStorage.setPathToDoom64ModsFolder( selectedPath) }
                },
                previousPathToDoom64ModsFolder, requestOnlyDirectory = true
            )
        }
    }

    private fun getPathToDoom64UserFolder() = pathToRootUserFolder + File.separator + "doom64ex-plus" + File.separator

    private suspend fun getPathToDoom64ModsFolder(): String {
        val enableDoom64Mods = preferencesStorage.enableDoom64Mods.first()

        if (!enableDoom64Mods) {
            return ""
        }

        var pathToDoom64ModsFolder = preferencesStorage.pathToDoom64ModsFolder.first()

        val pathToDoom64ModsFolderExists = File(pathToDoom64ModsFolder).exists()

        if (!pathToDoom64ModsFolderExists) {
            pathToDoom64ModsFolder = ""
        }

        return pathToDoom64ModsFolder
    }
}