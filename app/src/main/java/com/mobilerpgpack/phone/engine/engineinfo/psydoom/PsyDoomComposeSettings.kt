package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.ui.items.RequestPath
import com.mobilerpgpack.phone.ui.items.RequestPathMode
import com.mobilerpgpack.phone.ui.screen.screencontrols.wolfensteinButtons
import com.mobilerpgpack.phone.utils.IAssetExtractor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class PsyDoomComposeSettings : IEngineUIController, KoinComponent {

    private val assetsExtractor : IAssetExtractor by inject ()

    private val preferencesStorage : PsyDoomPreferencesStorage by inject (
        named(EngineTypes.PsyDoom.toString()))

    override val screenViewsToDraw = wolfensteinButtons

    @Composable
    override fun DrawSettings() {
        if (assetsExtractor.assetsCopied){
            DrawPsyDoomCommonSettings()
        }
    }

    @Composable
    private fun DrawPsyDoomCommonSettings(){
        RequestPath(
            stringResource(R.string.path_to_psydoom_cue_file),
            preferencesStorage.pathToPsyDoomCueFile,
            preferencesStorage.pathToPsyDoomCueFilePrefsKey,
            RequestPathMode.Cue)

        HorizontalDivider()

        RequestPath(
            stringResource(R.string.path_to_psydoom_mods_folder),
            preferencesStorage.pathToPsyDoomModsFolder,
            preferencesStorage.pathToPsyDoomModsFolderPrefsKey)
    }
}