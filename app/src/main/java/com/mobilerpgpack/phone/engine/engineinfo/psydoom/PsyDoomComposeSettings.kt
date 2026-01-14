package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.engine.engineinfo.IEngineUIController
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.DrawModsSupport
import com.mobilerpgpack.phone.engine.engineinfo.utils.ui.SettingScreen
import com.mobilerpgpack.phone.ui.items.EditTextItem
import com.mobilerpgpack.phone.ui.items.DrawTitleText
import com.mobilerpgpack.phone.ui.items.SwitchItem
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawCommandLinePreferences
import com.mobilerpgpack.phone.ui.items.prefsitems.DrawHorizontalDivider
import com.mobilerpgpack.phone.ui.items.prefsitems.EditTextPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.ListPreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.PreferenceItem
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPath
import com.mobilerpgpack.phone.ui.items.prefsitems.RequestPathMode
import com.mobilerpgpack.phone.ui.items.prefsitems.SwitchPreferenceItem
import com.mobilerpgpack.phone.utils.getComposableValue
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class PsyDoomComposeSettings : IEngineUIController, KoinComponent {

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val viewModel: PsyDoomComposeSettingsViewModel = koinViewModel()
        val showView by rememberSaveable (viewModel.showView) {
            mutableStateOf(viewModel.showView) }

        if (showView) {
            DrawPsyDoomCommonSettings(viewModel,navController)
        }
    }

    @Composable
    private fun DrawPsyDoomCommonSettings(viewModel: PsyDoomComposeSettingsViewModel,
                                          navController: NavHostController) {
        val engineInfo : IEngineInfo  = koinInject (named(EngineTypes.PsyDoom.name))
        val preferencesStorage: PsyDoomPreferencesStorage = koinInject(named(EngineTypes.PsyDoom.name))

        DrawCommandLinePreferences(
            preferencesStorage.psyDoomCommandLineArgsString,
            preferencesStorage.psyDoomCommandLineArgsPrefsKey.name
        )

        DrawHorizontalDivider()

        RequestPath(
            stringResource(R.string.path_to_psydoom_cue_file),
            preferencesStorage.pathToPsyDoomCueFile,
            preferencesStorage.pathToPsyDoomCueFilePrefsKey,
            RequestPathMode.File,
            requiredFileExtensions = engineInfo.requiredResourceExtensions
        )

        DrawModsSupport(viewModel.modsModel)

        val enablePsyDoomMods = preferencesStorage.enablePsyDoomMods.getComposableValue()

        SwitchPreferenceItem(
            stringResource(R.string.enable_psydoom_mods),
            enablePsyDoomMods,
            preferencesStorage.enablePsyDoomModsPrefsKey.name)

        DrawHorizontalDivider()

        if (enablePsyDoomMods) {
            RequestPath(
                stringResource(R.string.path_to_psydoom_mods_folder),
                preferencesStorage.pathToPsyDoomModsFolder,
                preferencesStorage.pathToPsyDoomModsFolderPrefsKey
            )
            DrawHorizontalDivider()
        }
        PreferenceItem(stringResource(R.string.psydoom_more_settings)) {
            navController.navigate(MORE_SETTINGS_SCREEN)
        }
    }

    @Composable
    private fun DrawMoreSettings(navController: NavHostController) {
        DrawHorizontalDivider()
        PreferenceItem(stringResource(R.string.psydoom_launcher_settings)) {
            navController.navigate(LAUNCHER_SETTINGS_SCREEN)
        }
        DrawHorizontalDivider()
        PreferenceItem(stringResource(R.string.psydoom_graphics_settings)) {
            navController.navigate(GRAPHICS_SETTINGS_SCREEN)
        }
        DrawHorizontalDivider()
        PreferenceItem(stringResource(R.string.psydoom_game)) {
            navController.navigate(GAME_SETTINGS_SCREEN)
        }
        DrawHorizontalDivider()
        PreferenceItem(stringResource(R.string.psydoom_input)) {
            navController.navigate(INPUT_SETTINGS_SCREEN)
        }
        DrawHorizontalDivider()
        PreferenceItem(stringResource(R.string.psydoom_audio)) {
            navController.navigate(AUDIO_SETTINGS_SCREEN)
        }
        DrawHorizontalDivider()
        PreferenceItem(stringResource(R.string.psydoom_cheats)) {
            navController.navigate(CHEATS_SETTINGS_SCREEN)
        }
        DrawHorizontalDivider()
        PreferenceItem(stringResource(R.string.psydoom_multiplayer)) {
            navController.navigate(MULTIPLAYER_SETTINGS_SCREEN)
        }
        DrawHorizontalDivider()
    }

    @Composable
    private fun DrawLauncherSettings() {
        val preferencesStorage: PsyDoomPreferencesStorage = koinInject(named(EngineTypes.PsyDoom.name))
        DrawTitleText(stringResource(R.string.psydoom_launcher_settings))

        SwitchPreferenceItem(
            stringResource(R.string.psydoom_record_demos),
            preferencesStorage.recordDemos, preferencesStorage.recordDemosPrefsKey.name
        )

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.psydoom_force_pistol_start),
            preferencesStorage.forcePistolStart, preferencesStorage.forcePistolStartPrefsKey.name
        )

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.psydoom_force_turbo_mode),
            preferencesStorage.turboMode, preferencesStorage.turboModePrefsKey.name
        )

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.psydoom_force_no_monsters),
            preferencesStorage.noMonsters, preferencesStorage.noMonstersPrefsKey.name
        )

        DrawHorizontalDivider()

        SwitchPreferenceItem(
            stringResource(R.string.psydoom_nm_boss_fixup),
            preferencesStorage.nmBossFixUp, preferencesStorage.nmBossFixUpPrefsKey.name
        )

        DrawHorizontalDivider()

        DrawNetworkSettings()
    }

    @Composable
    private fun DrawNetworkSettings() {
        val preferencesStorage: PsyDoomPreferencesStorage = koinInject(named(EngineTypes.PsyDoom.name))
        DrawTitleText(stringResource(R.string.psydoom_network_settings_title))

        EditTextPreferenceItem(
            stringResource(R.string.psydoom_host),
            preferencesStorage.host, preferencesStorage.hostPrefsKey.name
        )

        DrawHorizontalDivider()

        EditTextPreferenceItem(
            stringResource(R.string.psydoom_port),
            preferencesStorage.port,
            preferencesStorage.portPrefsKey.name
        )

        DrawHorizontalDivider()

        val peerTypeString = preferencesStorage.peerType.getComposableValue(PeerType.Client.name)

        val peerType by rememberSaveable (peerTypeString) {
            mutableStateOf(enumValueOf<PeerType>(peerTypeString)) }

        ListPreferenceItem(
            stringResource(R.string.psydoom_peer_type), peerType) {
            preferencesStorage.setStringValue(preferencesStorage.peerTypePrefsKey, it.toString())
        }

        DrawHorizontalDivider()
    }

    @Composable
    private fun DrawGraphicsSettings() {
        val viewModel: PsyDoomComposeSettingsViewModel = koinViewModel()

        DrawTitleText(stringResource(R.string.psydoom_graphics_settings))

        SwitchItem(stringResource(R.string.psydoom_enable_vsync), viewModel.enableVsync) {
            viewModel.enableVsync = it
        }

        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_refresh_rate),
            viewModel.outputRefreshRate, ) {
            viewModel.outputRefreshRate = it
        }
        DrawHorizontalDivider()
        DrawTitleText(stringResource(R.string.psydoom_picture_crop_settings))

        EditTextItem(
            stringResource(R.string.psydoom_top_overscan_pixels),
            viewModel.topOverscanPixels, 
        ) {
            viewModel.topOverscanPixels = it
        }

        DrawHorizontalDivider()

        EditTextItem(
            stringResource(R.string.psydoom_bottom_overscan_pixels),
            viewModel.bottomOverscanPixels, 
        ) {
            viewModel.bottomOverscanPixels = it
        }

        DrawHorizontalDivider()

        EditTextItem(
            stringResource(R.string.psydoom_logical_display_width),
            viewModel.logicalDisplayWidth, 
        ) {
            viewModel.logicalDisplayWidth = it
        }

        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_general_settings))
        EditTextItem(stringResource(R.string.psydoom_vram_size),
            viewModel.vramSizeInMbytes, ) {
            viewModel.vramSizeInMbytes = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_extended_automap_colors),
            viewModel.useExtendedAutomapColors
        ) {
            viewModel.useExtendedAutomapColors = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_vulkan_render))

        EditTextItem(stringResource(R.string.psydoom_render_height),
            viewModel.renderHeight, ) {
            viewModel.renderHeight = it
        }
        DrawHorizontalDivider()
        EditTextItem(
            stringResource(R.string.psydoom_anti_aliasing_samples),
            viewModel.antialiasingMultisamples, 
        ) {
            viewModel.antialiasingMultisamples = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_mimic_original_pixel_stretch),
            viewModel.vulkanPixelsStretch
        ) {
            viewModel.vulkanPixelsStretch = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_widescreen),
            viewModel.widescreenEnabled
        ) {
            viewModel.widescreenEnabled = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_extended_status_bar),
            viewModel.drawExtendedStatusBar
        ) {
            viewModel.drawExtendedStatusBar = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_disable_vulkan_render),
            viewModel.disableVulkanRender
        ) {
            viewModel.disableVulkanRender = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_triple_buffer),
            viewModel.tripleBuffer
        ) {
            viewModel.tripleBuffer = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_shading),
            viewModel.use32bitShading
        ) {
            viewModel.use32bitShading = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_brighten_automap),
            viewModel.brightenAutomap
        ) {
            viewModel.brightenAutomap = it
        }
        DrawHorizontalDivider()
        DrawTitleText(stringResource(R.string.psydoom_classic_render))
        SwitchItem(
            stringResource(R.string.psydoom_enhance_draw_wall_precision),
            viewModel.enhanceWallDrawPrecision
        ) {
            viewModel.enhanceWallDrawPrecision = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_floor_render_gap_fix),
            viewModel.floorGapRenderFix
        ) {
            viewModel.floorGapRenderFix = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_sky_leak_fix),
            viewModel.skyLeakFix
        ) {
            viewModel.skyLeakFix = it
        }
        DrawHorizontalDivider()
    }

    @Composable
    private fun DrawGameSettings() {
        val viewModel: PsyDoomComposeSettingsViewModel = koinViewModel()
        DrawTitleText(stringResource(R.string.psydoom_interpolation))
        SwitchItem(
            stringResource(R.string.psydoom_interpolate_sectors),
            viewModel.interpolateSectors
        ) {
            viewModel.interpolateSectors = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_interpolate_monsters),
            viewModel.interpolateMonsters
        ) {
            viewModel.interpolateMonsters = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_interpolate_things),
            viewModel.interpolateThings
        ) {
            viewModel.interpolateThings = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_interpolate_weapon),
            viewModel.interpolateWeapon
        ) {
            viewModel.interpolateWeapon = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_counters))

        SwitchItem(
            stringResource(R.string.psydoom_level_timer),
            viewModel.enableLevelTimer
        ) {
            viewModel.enableLevelTimer = it
        }
        DrawHorizontalDivider()

        SwitchItem(
            stringResource(R.string.psydoom_perf_counters),
            viewModel.showPerfCounters
        ) {
            viewModel.showPerfCounters = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_miscellaneous))

        SwitchItem(
            stringResource(R.string.psydoom_pause_on_focus_lost),
            viewModel.pauseOnFocusLost
        ) {
            viewModel.pauseOnFocusLost = it
        }
        DrawHorizontalDivider()

        EditTextItem(
            stringResource(R.string.psydoom_bob_scale),
            viewModel.bobScale, 
        ) {
            viewModel.bobScale = it
        }
        DrawHorizontalDivider()
        EditTextItem(
            stringResource(R.string.psydoom_heap_size),
            viewModel.heapSize, 
        ) {
            viewModel.heapSize = it
        }
        DrawHorizontalDivider()
        DrawBugFixes(viewModel)
        DrawHorizontalDivider()
        DrawTweaks(viewModel)
        DrawHorizontalDivider()
        DrawExtraGameSettings(viewModel)
        DrawHorizontalDivider()
    }

    @Composable
    private fun DrawBugFixes(viewModel: PsyDoomComposeSettingsViewModel) {
        DrawTitleText(stringResource(R.string.psydoom_bug_fixes_to_apply))

        SwitchItem(
            stringResource(R.string.psydoom_fix_line_activation),
            viewModel.fixLineActivation
        ) {
            viewModel.fixLineActivation = it
        }
        DrawHorizontalDivider()

        SwitchItem(
            stringResource(R.string.psydoom_item_pickup_fix),
            viewModel.itemPickupFix
        ) {
            viewModel.itemPickupFix = it
        }
        DrawHorizontalDivider()

        SwitchItem(
            stringResource(R.string.psydoom_fix_mutiline_crossing),
            viewModel.fixMultiLineCrossing
        ) {
            viewModel.fixMultiLineCrossing = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_kill_count),
            viewModel.fixKillCount
        ) {
            viewModel.fixKillCount = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_player_rocket_blast_fix),
            viewModel.playerRocketBlastFix
        ) {
            viewModel.playerRocketBlastFix = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_sprite_vertical_warp),
            viewModel.fixSpriteVerticalWarp
        ) {
            viewModel.fixSpriteVerticalWarp = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_view_bob_strength),
            viewModel.fixViewBobStrength
        ) {
            viewModel.fixViewBobStrength = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_gravity_strength),
            viewModel.fixGravityStrength
        ) {
            viewModel.fixGravityStrength = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_lost_soul_spawn_fix),
            viewModel.useLostSoulSpawnFix
        ) {
            viewModel.useLostSoulSpawnFix = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_line_of_sight_overflow_fix),
            viewModel.useLineOfSightOverflowFix
        ) {
            viewModel.useLineOfSightOverflowFix = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_outdoor_bullet_pufs),
            viewModel.fixOutdoorBulletPuffs
        ) {
            viewModel.fixOutdoorBulletPuffs = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_blockng_gibs_bug),
            viewModel.fixBlockingGibsBug
        ) {
            viewModel.fixBlockingGibsBug = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_sound_propagation),
            viewModel.fixSoundPropagation) {
            viewModel.fixSoundPropagation = it
        }
    }

    @Composable
    private fun DrawTweaks (viewModel: PsyDoomComposeSettingsViewModel){
        DrawTitleText(stringResource(R.string.psydoom_tweaks))
        SwitchItem(
            stringResource(R.string.psydoom_extended_shoot_range),
            viewModel.useExtendedPlayerShootRange) {
            viewModel.useExtendedPlayerShootRange = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_multimap_pickup),
            viewModel.allowMultiMapPickup) {
            viewModel.allowMultiMapPickup = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_input_latency_tweak),
            viewModel.useMoveInputLatencyTweak) {
            viewModel.useMoveInputLatencyTweak = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_ssg_delay_tweak),
            viewModel.useSuperShotgunDelayTweak) {
            viewModel.useSuperShotgunDelayTweak = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_allow_turning_cancel),
            viewModel.allowTurningCancellation) {
            viewModel.allowTurningCancellation = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_force_spawn_dm_things),
            viewModel.singlePlayerForceSpawnDmThings) {
            viewModel.singlePlayerForceSpawnDmThings = it
        }
        DrawHorizontalDivider()
        ListPreferenceItem(stringResource(R.string.psydoom_allow_movement_cancel),
            viewModel.allowMovementCancellation) {
            viewModel.allowMovementCancellation = it
        }
        DrawHorizontalDivider()
        ListPreferenceItem(stringResource(R.string.psydoom_final_doom_player_physx),
            viewModel.useFinalDoomPlayerMovement ) {
            viewModel.useFinalDoomPlayerMovement = it
        }
        DrawHorizontalDivider()
        EditTextItem(
            stringResource(R.string.psydoom_lost_soul_spawn_limit),
            viewModel.lostSoulSpawnLimit, ) {
            viewModel.lostSoulSpawnLimit = it
        }
    }

    @Composable
    private fun DrawExtraGameSettings(viewModel: PsyDoomComposeSettingsViewModel){
        DrawTitleText(stringResource(R.string.psydoom_map_patches_to_apply))
        SwitchItem(
            stringResource(R.string.psydoom_gameplay_fixes),
            viewModel.enableMapPatchesGamePlay) {
            viewModel.enableMapPatchesGamePlay = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_visual_fixes),
            viewModel.enableMapPatchesVisual) {
            viewModel.enableMapPatchesVisual = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fixes),
            viewModel.enableMapPatchesPsyDoom) {
            viewModel.enableMapPatchesPsyDoom = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_game_tick_rate))

        ListPreferenceItem(stringResource(R.string.psydoom_mode),
            viewModel.usePalTimings ) {
            viewModel.usePalTimings = it
        }
        DrawHorizontalDivider()

        SwitchItem(stringResource(R.string.psydoom_demo_timings),
            viewModel.useDemoTimings) {
            viewModel.useDemoTimings = it
        }
    }

    @Composable
    private fun DrawInputSettings(){
        val viewModel: PsyDoomComposeSettingsViewModel = koinViewModel()
        DrawTitleText(stringResource(R.string.psydoom_mouse))

        EditTextItem(stringResource(R.string.psydoom_mouse_turn_speed),
            viewModel.mouseTurnSpeed, ) {
            viewModel.mouseTurnSpeed = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_gamepad))

        EditTextItem(stringResource(R.string.psydoom_dead_zone),
            viewModel.gamepadDeadZone, ) {
            viewModel.gamepadDeadZone = it
        }
        DrawHorizontalDivider()

        EditTextItem(stringResource(R.string.psydoom_high_fast_turn_speed),
            viewModel.gamepadFastTurnSpeedHigh, ) {
            viewModel.gamepadFastTurnSpeedHigh = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_low_fast_turn_speed),
            viewModel.gamepadFastTurnSpeedLow, ) {
            viewModel.gamepadFastTurnSpeedLow = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_low_turn_speed),
            viewModel.gamepadTurnSpeedLow, ) {
            viewModel.gamepadTurnSpeedLow = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_high_turn_speed),
            viewModel.gamepadTurnSpeedHigh, ) {
            viewModel.gamepadTurnSpeedHigh = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_analog_to_digital_threshold),
            viewModel.analogToDigitalThreshold, ) {
            viewModel.analogToDigitalThreshold = it
        }
        DrawHorizontalDivider()
    }

    @Composable
    private fun DrawAudioSettings(){
        val viewModel: PsyDoomComposeSettingsViewModel = koinViewModel()
        DrawTitleText(stringResource(R.string.psydoom_audio))

        EditTextItem(stringResource(R.string.psydoom_audio_buffer_size),
            viewModel.audioBufferSize, ) {
            viewModel.audioBufferSize = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_spu_ram_size),
            viewModel.spuRamSize, ) {
            viewModel.spuRamSize = it
        }
        DrawHorizontalDivider()
    }

    @Composable
    private fun DrawCheatsScreen(){
        val viewModel: PsyDoomComposeSettingsViewModel = koinViewModel()
        DrawTitleText(stringResource(R.string.psydoom_cheats_general))

        SwitchItem(stringResource(R.string.psydoom_developer_cheats_shortcuts),
            viewModel.enableDevCheatShortcuts) {
            viewModel.enableDevCheatShortcuts = it
        }
        DrawHorizontalDivider()
        SwitchItem(stringResource(R.string.psydoom_developer_map_reload_key),
            viewModel.enableDevInPlaceReloadFunctionKey) {
            viewModel.enableDevInPlaceReloadFunctionKey = it
        }
        DrawHorizontalDivider()
        SwitchItem(stringResource(R.string.psydoom_developer_map_reload),
            viewModel.enableDevMapAutoReload) {
            viewModel.enableDevMapAutoReload = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_cheat_key_sequences))
        EditTextItem(stringResource(R.string.psydoom_god_mode),
            viewModel.cheatKeySequenceGodMode, ) {
            viewModel.cheatKeySequenceGodMode = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_no_clip),
            viewModel.cheatKeySequenceNoClip, ) {
            viewModel.cheatKeySequenceNoClip = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_level_warp),
            viewModel.cheatKeySequenceLevelWarp, ) {
            viewModel.cheatKeySequenceLevelWarp = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_weapons_keys_armor_cheat),
            viewModel.cheatKeySequenceWeaponsKeysAndArmor, ) {
            viewModel.cheatKeySequenceWeaponsKeysAndArmor = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_weapons_armor_cheat),
            viewModel.cheatKeySequenceWeaponsAndArmor, ) {
            viewModel.cheatKeySequenceWeaponsAndArmor = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_map_lines_on),
            viewModel.cheatKeySequenceAllMapLinesOn, ) {
            viewModel.cheatKeySequenceAllMapLinesOn = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_map_things_on),
            viewModel.cheatKeySequenceAllMapThingsOn, ) {
            viewModel.cheatKeySequenceAllMapThingsOn = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_xray_vision),
            viewModel.cheatKeySequenceXRayVision, ) {
            viewModel.cheatKeySequenceXRayVision = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_vram_viewer),
            viewModel.cheatKeySequenceVramViewer, ) {
            viewModel.cheatKeySequenceVramViewer = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_no_target),
            viewModel.cheatKeySequenceNoTarget, ) {
            viewModel.cheatKeySequenceNoTarget = it
        }
        DrawHorizontalDivider()
    }

    @Composable
    private fun DrawMultiplayerScreen(){
        val viewModel: PsyDoomComposeSettingsViewModel = koinViewModel()
        DrawTitleText(stringResource(R.string.psydoom_cooperative))

        SwitchItem(stringResource(R.string.psydoom_no_friendly_fire),
            viewModel.coopNoFriendlyFire) {
            viewModel.coopNoFriendlyFire = it
        }
        DrawHorizontalDivider()

        SwitchItem(stringResource(R.string.psydoom_spawn_deathmatch_only_things),
            viewModel.coopForceSpawnDeathmatchThings) {
            viewModel.coopForceSpawnDeathmatchThings = it
        }
        DrawHorizontalDivider()

        SwitchItem(stringResource(R.string.psydoom_preserve_keys_on_respawn),
            viewModel.coopPreserveKeys) {
            viewModel.coopPreserveKeys = it
        }
        DrawHorizontalDivider()
        SwitchItem(stringResource(R.string.psydoom_preserve_weapons_on_respawn),
            viewModel.coopPreserveWeapons) {
            viewModel.coopPreserveWeapons = it
        }
        DrawHorizontalDivider()

        ListPreferenceItem(stringResource(R.string.psydoom_preserve_ammo_on_respawn),
            viewModel.coopPreserveAmmoFactor,) {
            viewModel.coopPreserveAmmoFactor = it
        }
        DrawHorizontalDivider()

        SwitchItem(stringResource(R.string.psydoom_preserve_weapons_on_respawn),
            viewModel.coopPreserveWeapons) {
            viewModel.coopPreserveWeapons = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_deathmatch))

        EditTextItem(stringResource(R.string.psydoom_frag_limit),
            viewModel.dmFragLimit, ) {
            viewModel.dmFragLimit = it
        }

        DrawHorizontalDivider()
        SwitchItem(stringResource(R.string.psydoom_disable_exits),
            viewModel.dmExitDisabled) {
            viewModel.dmExitDisabled = it
        }
        DrawHorizontalDivider()
        SwitchItem(stringResource(R.string.psydoom_auto_activate_boss),
            viewModel.dmActivateBossSpecialSectors) {
            viewModel.dmActivateBossSpecialSectors = it
        }
        DrawHorizontalDivider()
    }

    data class PsyDoomLauncherSettingsScreen(private val psyDoomComposeSettings: PsyDoomComposeSettings) :
        SettingScreen(LAUNCHER_SETTINGS_SCREEN) {

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            psyDoomComposeSettings.DrawLauncherSettings()
    }

    data class PsyDoomGraphicsSettingsScreen(private val psyDoomComposeSettings: PsyDoomComposeSettings) :
        SettingScreen(GRAPHICS_SETTINGS_SCREEN) {

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            psyDoomComposeSettings.DrawGraphicsSettings()
    }

    data class PsyDoomInputSettingsScreen(private val psyDoomComposeSettings: PsyDoomComposeSettings) :
        SettingScreen(INPUT_SETTINGS_SCREEN) {

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            psyDoomComposeSettings.DrawInputSettings()
    }

    data class PsyDoomCheatsSettingsScreen(private val psyDoomComposeSettings: PsyDoomComposeSettings) :
        SettingScreen(CHEATS_SETTINGS_SCREEN) {

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            psyDoomComposeSettings.DrawCheatsScreen()
    }

    data class PsyDoomMultiplayerSettingsScreen(private val psyDoomComposeSettings: PsyDoomComposeSettings) :
        SettingScreen(MULTIPLAYER_SETTINGS_SCREEN) {

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            psyDoomComposeSettings.DrawMultiplayerScreen()
    }

    data class PsyDoomAudioSettingsScreen(private val psyDoomComposeSettings: PsyDoomComposeSettings) :
        SettingScreen(AUDIO_SETTINGS_SCREEN) {

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            psyDoomComposeSettings.DrawAudioSettings()
    }

    data class PsyDoomGameSettingsScreen(private val psyDoomComposeSettings: PsyDoomComposeSettings) :
        SettingScreen(GAME_SETTINGS_SCREEN) {

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            psyDoomComposeSettings.DrawGameSettings()
    }

    data class PsyDoomMoreSettingsScreen(private val psyDoomComposeSettings: PsyDoomComposeSettings) :
        SettingScreen(MORE_SETTINGS_SCREEN) {

        @Composable
        override fun DrawSettingsScreen(navController: NavHostController) =
            psyDoomComposeSettings.DrawMoreSettings(navController)
    }

    enum class GameEnum (val value: Int){
        Never (0),
        Always(1),
        Auto (-1);

        companion object {
            fun fromValue(value: Int): GameEnum? {
                return entries.find { it.value == value }
            }
        }
    }

    enum class TickMode (val value: Int){
        NTSC (0),
        PAL(1),
        Auto (-1);

        companion object {
            fun fromValue(value: Int): TickMode? {
                return entries.find { it.value == value }
            }
        }
    }

    enum class RespawnAmmoEnum (val value: Int){
        NONE (0),
        ALL(1),
        HALF (2);

        companion object {
            fun fromValue(value: Int): RespawnAmmoEnum? {
                return entries.find { it.value == value }
            }
        }
    }

    private companion object {
        private const val LAUNCHER_SETTINGS_SCREEN = "launcher_settings_screen"
        private const val MORE_SETTINGS_SCREEN = "more_settings_screen"
        private const val GRAPHICS_SETTINGS_SCREEN = "graphics_screen"
        private const val GAME_SETTINGS_SCREEN = "game_screen"
        private const val INPUT_SETTINGS_SCREEN = "input_screen"
        private const val AUDIO_SETTINGS_SCREEN = "audio_screen"
        private const val CHEATS_SETTINGS_SCREEN = "cheats_screen"
        private const val MULTIPLAYER_SETTINGS_SCREEN = "multiplayer_screen"
    }
}





