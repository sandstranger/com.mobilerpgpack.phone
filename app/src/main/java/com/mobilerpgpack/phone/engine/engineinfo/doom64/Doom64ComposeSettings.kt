package com.mobilerpgpack.phone.engine.engineinfo.doom64

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.DrawModsSupport
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named

class Doom64ComposeSettings () :
    KoinComponent, IEngineUIController {

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val preferencesStorage: PreferencesStorage = koinInject()
        preferencesStorage.apply {
            val modsModel: ModsModel = koinInject(named(EngineTypes.Doom64ExPlus.name))
            val previousPathToDoom64WadsFolder = pathToDoom64MainWadsFolder
            DrawCommandLinePreferences(
                doom64CommandLineArgsString,
                doom64CommandLineArgsStringPrefsKey.name
            )

            DrawHorizontalDivider()

            RequestPath(
                stringResource(R.string.path_to_doom64_folder),
                previousPathToDoom64WadsFolder
            ) { selectedPath ->
                setPathToDoom64MainWadsFolder(selectedPath)
            }

            DrawHorizontalDivider()

            val networkMode = doom64NetworkMode.getComposableValue(Doom64NetworkMode.Disabled)
            val networkModeOptions = listOf(
                stringResource(R.string.doom64_coop_disabled),
                stringResource(R.string.doom64_coop_host),
                stringResource(R.string.doom64_coop_join),
                stringResource(R.string.doom64_coop_autojoin)
            )
            val selectedNetworkMode = when (networkMode) {
                Doom64NetworkMode.Disabled -> networkModeOptions[0]
                Doom64NetworkMode.Host -> networkModeOptions[1]
                Doom64NetworkMode.Join -> networkModeOptions[2]
                Doom64NetworkMode.AutoJoin -> networkModeOptions[3]
            }

            ListPreferenceItem(
                stringResource(R.string.doom64_coop_mode),
                selectedNetworkMode,
                networkModeOptions
            ) { selectedMode ->
                val newMode = when (selectedMode) {
                    networkModeOptions[1] -> Doom64NetworkMode.Host
                    networkModeOptions[2] -> Doom64NetworkMode.Join
                    networkModeOptions[3] -> Doom64NetworkMode.AutoJoin
                    else -> Doom64NetworkMode.Disabled
                }
                setEnumValue(doom64NetworkModePrefsKey, newMode)
            }

            if (networkMode != Doom64NetworkMode.Disabled) {
                DrawHorizontalDivider()

                EditTextPreferenceItem(
                    stringResource(R.string.doom64_coop_player_name),
                    doom64CoopPlayerName,
                    doom64CoopPlayerNamePrefsKey.name
                )

                DrawHorizontalDivider()

                EditTextPreferenceItem(
                    stringResource(R.string.doom64_coop_port),
                    doom64CoopPort,
                    doom64CoopPortPrefsKey.name
                ) {
                    setIntValue(doom64CoopPortPrefsKey, it.coerceIn(1, 65535))
                }

                when (networkMode) {
                    Doom64NetworkMode.Disabled -> Unit

                    Doom64NetworkMode.Host -> {
                        DrawHorizontalDivider()
                        EditTextPreferenceItem(
                            stringResource(R.string.doom64_coop_players_to_start),
                            doom64CoopPlayersToStart,
                            doom64CoopPlayersToStartPrefsKey.name
                        ) {
                            setIntValue(doom64CoopPlayersToStartPrefsKey, it.coerceIn(1, 4))
                        }

                        DrawHorizontalDivider()
                        EditTextPreferenceItem(
                            stringResource(R.string.doom64_coop_map),
                            doom64CoopMap,
                            doom64CoopMapPrefsKey.name
                        ) {
                            setIntValue(doom64CoopMapPrefsKey, it.coerceIn(1, 32))
                        }

                        DrawHorizontalDivider()
                        EditTextPreferenceItem(
                            stringResource(R.string.doom64_coop_skill),
                            doom64CoopSkill,
                            doom64CoopSkillPrefsKey.name
                        ) {
                            setIntValue(doom64CoopSkillPrefsKey, it.coerceIn(1, 5))
                        }

                        DrawHorizontalDivider()
                        SwitchPreferenceItem(
                            stringResource(R.string.doom64_coop_deathmatch),
                            doom64CoopDeathmatch,
                            doom64CoopDeathmatchPrefsKey.name
                        )

                        DrawHorizontalDivider()
                        SwitchPreferenceItem(
                            stringResource(R.string.doom64_coop_respawn_monsters),
                            doom64CoopRespawnMonsters,
                            doom64CoopRespawnMonstersPrefsKey.name
                        )

                        DrawHorizontalDivider()
                        SwitchPreferenceItem(
                            stringResource(R.string.doom64_coop_respawn_items),
                            doom64CoopRespawnItems,
                            doom64CoopRespawnItemsPrefsKey.name
                        )

                        DrawHorizontalDivider()
                        SwitchPreferenceItem(
                            stringResource(R.string.doom64_coop_fast_monsters),
                            doom64CoopFastMonsters,
                            doom64CoopFastMonstersPrefsKey.name
                        )

                        DrawHorizontalDivider()
                        SwitchPreferenceItem(
                            stringResource(R.string.doom64_coop_no_monsters),
                            doom64CoopNoMonsters,
                            doom64CoopNoMonstersPrefsKey.name
                        )

                        DrawHorizontalDivider()
                        SwitchPreferenceItem(
                            stringResource(R.string.doom64_coop_friendly_fire),
                            doom64CoopFriendlyFire,
                            doom64CoopFriendlyFirePrefsKey.name
                        )

                        DrawHorizontalDivider()
                        SwitchPreferenceItem(
                            stringResource(R.string.doom64_coop_keep_items),
                            doom64CoopKeepItems,
                            doom64CoopKeepItemsPrefsKey.name
                        )
                    }

                    Doom64NetworkMode.Join -> {
                        DrawHorizontalDivider()
                        EditTextPreferenceItem(
                            stringResource(R.string.doom64_coop_server_address),
                            doom64CoopServerAddress,
                            doom64CoopServerAddressPrefsKey.name,
                            stringResource(R.string.doom64_coop_server_address_hint)
                        )
                    }

                    Doom64NetworkMode.AutoJoin -> Unit
                }
            }

            DrawModsSupport(modsModel)

            val enableDoom64Mods = enableDoom64Mods.getComposableValue()

            SwitchPreferenceItem(
                stringResource(R.string.enable_doom64_mods),
                initialValue = enableDoom64Mods,
                enableDoom64ModsPrefsKey.name
            )

            if (enableDoom64Mods) {
                DrawHorizontalDivider()

                RequestPath(
                    stringResource(R.string.path_to_doom64_mods_folder),
                    pathToDoom64ModsFolder,
                ) { selectedPath ->
                    setPathToDoom64ModsFolder(selectedPath)
                }
            }
            DrawHorizontalDivider()
            EditTextPreferenceItem(stringResource(R.string.anisotropy_level), doom64AnisotropyTexturesValue) {
                setIntValue(doom64AnisotropyTexturesValuePrefsKey, it.coerceIn(1, 16))
            }
        }
    }
}