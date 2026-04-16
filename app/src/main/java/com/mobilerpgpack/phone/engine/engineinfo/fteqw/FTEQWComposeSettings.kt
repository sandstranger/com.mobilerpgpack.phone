package com.mobilerpgpack.phone.engine.engineinfo.fteqw

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UZDoomRenderAPI
import com.mobilerpgpack.phone.ui.getButtonsColors
import com.mobilerpgpack.phone.ui.getOnPrimaryColor
import com.mobilerpgpack.phone.ui.items.EditTextItem
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.compose.koinInject
import org.koin.core.qualifier.named

class FTEQWComposeSettings : IEngineUIController {
    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val prefsStorage : FTEQWPreferencesStorage = koinInject(named((EngineTypes.FTEQW.name)))
        val activeFTEQWGame = prefsStorage.activeFTEQWGame.getComposableValue(FTEQWGames.Quake)
        val enableModsSupport = prefsStorage.enableFTEQWModsSupport.getComposableValue()
        val enableCustomManifestSupport = prefsStorage.enableManifestSupport.getComposableValue()
        val buttonsColors = getButtonsColors()
        val onPrimaryColor = getOnPrimaryColor()
        val allowedManifestExtensions = retain { listOf(".fmf", ".FMF") }
        val quake2GameTypeStream = prefsStorage.quake2GameType.getComposableValue(Quake2Games.DefaultGame)
        val quake2GameTypeDescription by remember(quake2GameTypeStream) {
            mutableStateOf(quake2GameTypeStream.description) }
        val quake2GameTypesDescriptions = retain { Quake2Games.descriptions }

        DrawCommandLinePreferences(prefsStorage.commandLineArgs,
            prefsStorage.commandLineArgsPrefsKey.name)

        DrawHorizontalDivider()

        EditTextPreferenceItem(stringResource(R.string.ui_scale), prefsStorage.fteqwUIScale){
            prefsStorage.setFloatValue(prefsStorage.fteqwUIScalePrefsKey, it.coerceAtLeast(2.0f))
        }

        DrawHorizontalDivider()

        ListPreferenceItem(stringResource(R.string.fteqw_game),
            activeFTEQWGame){
            prefsStorage.setEnumValue(prefsStorage.activeFTEQWGamePrefsKey,it)
        }

        DrawHorizontalDivider()

        when (activeFTEQWGame) {
            FTEQWGames.Quake -> {
                RequestPath(stringResource(R.string.path_to_quake1_root_dir),
                    prefsStorage.pathToQuake1,
                    prefsStorage.pathToQuake1PrefsKey, requestMode = RequestPathMode.Directory)

                DrawHorizontalDivider()

                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally)) {

                    Box(modifier = Modifier.weight(1.0f)){
                        RequestPath(stringResource(R.string.path_to_quake1_base_dir),
                            prefsStorage.pathToQuake1BaseDir,
                            prefsStorage.pathToQuake1BaseDirPrefsKey, requestMode = RequestPathMode.Directory)
                    }

                    Button(modifier = Modifier.padding(end = 4.dp), onClick = {
                        prefsStorage.setStringValue(prefsStorage.pathToQuake1BaseDirPrefsKey, "")
                    }, colors = buttonsColors) {
                        Text(
                            text = stringResource(R.string.clear),
                            textAlign = TextAlign.Center,
                            color = onPrimaryColor
                        )
                    }
                }
            }
            FTEQWGames.Quake2 -> {
                ListPreferenceItem(stringResource(R.string.quake2_game_type),
                    quake2GameTypeDescription, quake2GameTypesDescriptions){
                    prefsStorage.setEnumValue(prefsStorage.quake2GameTypePrefsKey,
                        Quake2Games.fromValue(it))
                }

                DrawHorizontalDivider()

                RequestPath(stringResource(R.string.path_to_quake2_root_dir),
                    prefsStorage.pathToQuake2,
                    prefsStorage.pathToQuake2PrefsKey, requestMode = RequestPathMode.Directory)

                DrawHorizontalDivider()

                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally)) {

                    Box(modifier = Modifier.weight(1.0f)){
                        RequestPath(stringResource(R.string.path_to_quake2_base_dir),
                            prefsStorage.pathToQuake2BaseDir,
                            prefsStorage.pathToQuake2BaseDirPrefsKey, requestMode = RequestPathMode.Directory)
                    }

                    Button(modifier = Modifier.padding(end = 4.dp), onClick = {
                        prefsStorage.setStringValue(prefsStorage.pathToQuake2BaseDirPrefsKey, "")
                    }, colors = buttonsColors) {
                        Text(
                            text = stringResource(R.string.clear),
                            textAlign = TextAlign.Center,
                            color = onPrimaryColor
                        )
                    }
                }
            }
            FTEQWGames.Quake3 -> {
                RequestPath(stringResource(R.string.path_to_quake3_root_dir),
                    prefsStorage.pathToQuake3,
                    prefsStorage.pathToQuake3PrefsKey, requestMode = RequestPathMode.Directory)

                DrawHorizontalDivider()

                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally)) {

                    Box(modifier = Modifier.weight(1.0f)){
                        RequestPath(stringResource(R.string.path_to_quake3_base_dir),
                            prefsStorage.pathToQuake3BaseDir,
                            prefsStorage.pathToQuake3BaseDirPrefsKey, requestMode = RequestPathMode.Directory)
                    }

                    Button(modifier = Modifier.padding(end = 4.dp), onClick = {
                        prefsStorage.setStringValue(prefsStorage.pathToQuake3BaseDirPrefsKey, "")
                    }, colors = buttonsColors) {
                        Text(
                            text = stringResource(R.string.clear),
                            textAlign = TextAlign.Center,
                            color = onPrimaryColor
                        )
                    }
                }
            }
            FTEQWGames.Hexen2 -> {
                RequestPath(stringResource(R.string.path_to_hexen2_root_dir),
                    prefsStorage.pathToHexen2,
                    prefsStorage.pathToHexen2PrefsKey, requestMode = RequestPathMode.Directory)

                DrawHorizontalDivider()

                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally)) {

                    Box(modifier = Modifier.weight(1.0f)){
                        RequestPath(stringResource(R.string.path_to_hexen2_base_dir),
                            prefsStorage.pathToHexen2BaseDir,
                            prefsStorage.pathToHexen2BaseDirPrefsKey, requestMode = RequestPathMode.Directory)
                    }

                    Button(modifier = Modifier.padding(end = 4.dp), onClick = {
                        prefsStorage.setStringValue(prefsStorage.pathToHexen2BaseDirPrefsKey, "")
                    }, colors = buttonsColors) {
                        Text(
                            text = stringResource(R.string.clear),
                            textAlign = TextAlign.Center,
                            color = onPrimaryColor
                        )
                    }
                }
            }
        }

        DrawHorizontalDivider()

        SwitchPreferenceItem(stringResource(R.string.enable_mods_support),
            enableModsSupport, prefsStorage.enableFTEQWModsPrefsKey .name)

        DrawHorizontalDivider()

        if (enableModsSupport){
            when (activeFTEQWGame) {
                FTEQWGames.Quake -> {
                    RequestPath(stringResource(R.string.path_to_quake1_mods_dir),
                        prefsStorage.pathToQuake1ModsDir,
                        prefsStorage.pathToQuake1ModsDirPrefsKey, requestMode = RequestPathMode.Directory)
                }
                FTEQWGames.Quake2 -> {
                    RequestPath(stringResource(R.string.path_to_quake2_mods_dir),
                        prefsStorage.pathToQuake2ModsDir,
                        prefsStorage.pathToQuake2ModsDirPrefsKey, requestMode = RequestPathMode.Directory)
                }
                FTEQWGames.Quake3 -> {
                    RequestPath(stringResource(R.string.path_to_quake3_mods_dir),
                        prefsStorage.pathToQuake3ModsDir,
                        prefsStorage.pathToQuake3ModsDirPrefsKey, requestMode = RequestPathMode.Directory)
                }
                FTEQWGames.Hexen2 -> {
                    RequestPath(stringResource(R.string.path_to_hexen2_mods_dir),
                        prefsStorage.pathToHexen2ModsDir,
                        prefsStorage.pathToHexen2ModsDirPrefsKey, requestMode = RequestPathMode.Directory)
                }
            }

            DrawHorizontalDivider()
        }

        SwitchPreferenceItem(stringResource(R.string.enable_fteqw_manifest_support),
            enableCustomManifestSupport, prefsStorage.enableManifestSupportPrefsKey.name)

        if (enableCustomManifestSupport){
            DrawHorizontalDivider()
            when (activeFTEQWGame) {
                FTEQWGames.Quake -> {
                    RequestPath(stringResource(R.string.path_to_quake1_manifest),
                        prefsStorage.pathToQuake1Manifest,
                        prefsStorage.pathToQuake1ManifestPrefsKey, requestMode = RequestPathMode.File,
                        requiredFileExtensions = allowedManifestExtensions)
                }
                FTEQWGames.Quake2 -> {
                    RequestPath(stringResource(R.string.path_to_quake2_manifest),
                        prefsStorage.pathToQuake2Manifest,
                        prefsStorage.pathToQuake2ManifestPrefsKey, requestMode = RequestPathMode.File,
                        requiredFileExtensions = allowedManifestExtensions)
                }
                FTEQWGames.Quake3 -> {
                    RequestPath(stringResource(R.string.path_to_quake3_manifest),
                        prefsStorage.pathToQuake3Manifest,
                        prefsStorage.pathToQuake3ManifestPrefsKey, requestMode = RequestPathMode.File,
                        requiredFileExtensions = allowedManifestExtensions)
                }
                FTEQWGames.Hexen2 -> {
                    RequestPath(stringResource(R.string.path_to_hexen2_manifest),
                        prefsStorage.pathToHexen2Manifest,
                        prefsStorage.pathToHexen2ManifestPrefsKey, requestMode = RequestPathMode.File,
                        requiredFileExtensions = allowedManifestExtensions)
                }
            }
        }
    }
}