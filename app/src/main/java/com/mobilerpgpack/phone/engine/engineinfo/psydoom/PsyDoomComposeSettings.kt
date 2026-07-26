package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import org.koin.core.qualifier.named

class PsyDoomComposeSettings : IEngineUIController, KoinComponent {

    @Composable
    override fun DrawSettings(navController: NavHostController) {
        val viewModel: PsyDoomComposeSettingsViewModel = koinViewModel()
        val showView = viewModel.showView.getComposableValue()

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
            stringResource(R.string.path_to_psydoom_cue_directory),
            preferencesStorage.pathToPsyDoomCueDirectory,
            preferencesStorage.pathToPsyDoomCueDirectoryPrefsKey,
            RequestPathMode.Directory,
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

        SwitchItem(stringResource(R.string.psydoom_enable_vsync), viewModel.enableVsyncAsLiveData) {
            viewModel.enableVsync = it
        }

        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_refresh_rate),
            viewModel.outputRefreshRateAsLiveData, ) {
            viewModel.outputRefreshRate = it
        }
        DrawHorizontalDivider()
        DrawTitleText(stringResource(R.string.psydoom_picture_crop_settings))

        EditTextItem(
            stringResource(R.string.psydoom_top_overscan_pixels),
            viewModel.topOverscanPixelsAsLiveData,
        ) {
            viewModel.topOverscanPixels = it
        }

        DrawHorizontalDivider()

        EditTextItem(
            stringResource(R.string.psydoom_bottom_overscan_pixels),
            viewModel.bottomOverscanPixelsAsLiveData,
        ) {
            viewModel.bottomOverscanPixels = it
        }

        DrawHorizontalDivider()

        EditTextItem(
            stringResource(R.string.psydoom_logical_display_width),
            viewModel.logicalDisplayWidthAsLiveData,
        ) {
            viewModel.logicalDisplayWidth = it
        }

        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_general_settings))
        EditTextItem(stringResource(R.string.psydoom_vram_size),
            viewModel.vramSizeInMbytesAsLiveData, ) {
            viewModel.vramSizeInMbytes = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_extended_automap_colors),
            viewModel.useExtendedAutomapColorsAsLiveData
        ) {
            viewModel.useExtendedAutomapColors = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_vulkan_render))

        EditTextItem(stringResource(R.string.psydoom_render_height),
            viewModel.renderHeightAsLiveData, ) {
            viewModel.renderHeight = it
        }
        DrawHorizontalDivider()
        EditTextItem(
            stringResource(R.string.psydoom_anti_aliasing_samples),
            viewModel.antialiasingMultisamplesAsLiveData,
        ) {
            viewModel.antialiasingMultisamples = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_mimic_original_pixel_stretch),
            viewModel.vulkanPixelsStretchAsLiveData
        ) {
            viewModel.vulkanPixelsStretch = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_widescreen),
            viewModel.widescreenEnabledAsLiveData
        ) {
            viewModel.widescreenEnabled = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_extended_status_bar),
            viewModel.drawExtendedStatusBarAsLiveData
        ) {
            viewModel.drawExtendedStatusBar = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_disable_vulkan_render),
            viewModel.disableVulkanRenderAsLiveData
        ) {
            viewModel.disableVulkanRender = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_triple_buffer),
            viewModel.tripleBufferAsLiveData
        ) {
            viewModel.tripleBuffer = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_shading),
            viewModel.use32bitShadingAsLiveData
        ) {
            viewModel.use32bitShading = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_brighten_automap),
            viewModel.brightenAutomapAsLiveData
        ) {
            viewModel.brightenAutomap = it
        }
        DrawHorizontalDivider()
        DrawTitleText(stringResource(R.string.psydoom_classic_render))
        SwitchItem(
            stringResource(R.string.psydoom_enhance_draw_wall_precision),
            viewModel.enhanceWallDrawPrecisionAsLiveData
        ) {
            viewModel.enhanceWallDrawPrecision = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_floor_render_gap_fix),
            viewModel.floorGapRenderFixAsLiveData
        ) {
            viewModel.floorGapRenderFix = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_sky_leak_fix),
            viewModel.skyLeakFixAsLiveData
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
            viewModel.interpolateSectorsAsLiveData
        ) {
            viewModel.interpolateSectors = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_interpolate_monsters),
            viewModel.interpolateMonstersAsLiveData
        ) {
            viewModel.interpolateMonsters = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_interpolate_things),
            viewModel.interpolateThingsAsLiveData
        ) {
            viewModel.interpolateThings = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_interpolate_weapon),
            viewModel.interpolateWeaponAsLiveData
        ) {
            viewModel.interpolateWeapon = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_counters))

        SwitchItem(
            stringResource(R.string.psydoom_level_timer),
            viewModel.enableLevelTimerAsLiveData
        ) {
            viewModel.enableLevelTimer = it
        }
        DrawHorizontalDivider()

        SwitchItem(
            stringResource(R.string.psydoom_perf_counters),
            viewModel.showPerfCountersAsLiveData
        ) {
            viewModel.showPerfCounters = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_miscellaneous))

        SwitchItem(
            stringResource(R.string.psydoom_pause_on_focus_lost),
            viewModel.pauseOnFocusLostAsLiveData
        ) {
            viewModel.pauseOnFocusLost = it
        }
        DrawHorizontalDivider()

        EditTextItem(
            stringResource(R.string.psydoom_bob_scale),
            viewModel.bobScaleAsLiveData,
        ) {
            viewModel.bobScale = it
        }
        DrawHorizontalDivider()
        EditTextItem(
            stringResource(R.string.psydoom_heap_size),
            viewModel.heapSizeAsLiveData,
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
            viewModel.fixLineActivationAsLiveData
        ) {
            viewModel.fixLineActivation = it
        }
        DrawHorizontalDivider()

        SwitchItem(
            stringResource(R.string.psydoom_item_pickup_fix),
            viewModel.itemPickupFixAsLiveData
        ) {
            viewModel.itemPickupFix = it
        }
        DrawHorizontalDivider()

        SwitchItem(
            stringResource(R.string.psydoom_fix_mutiline_crossing),
            viewModel.fixMultiLineCrossingAsLiveData
        ) {
            viewModel.fixMultiLineCrossing = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_kill_count),
            viewModel.fixKillCountAsLiveData
        ) {
            viewModel.fixKillCount = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_player_rocket_blast_fix),
            viewModel.playerRocketBlastFixAsLiveData
        ) {
            viewModel.playerRocketBlastFix = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_sprite_vertical_warp),
            viewModel.fixSpriteVerticalWarpAsLiveData
        ) {
            viewModel.fixSpriteVerticalWarp = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_view_bob_strength),
            viewModel.fixViewBobStrengthAsLiveData
        ) {
            viewModel.fixViewBobStrength = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_gravity_strength),
            viewModel.fixGravityStrengthAsLiveData
        ) {
            viewModel.fixGravityStrength = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_lost_soul_spawn_fix),
            viewModel.useLostSoulSpawnFixAsLiveData
        ) {
            viewModel.useLostSoulSpawnFix = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_line_of_sight_overflow_fix),
            viewModel.useLineOfSightOverflowFixAsLiveData
        ) {
            viewModel.useLineOfSightOverflowFix = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_outdoor_bullet_pufs),
            viewModel.fixOutdoorBulletPuffsAsLiveData
        ) {
            viewModel.fixOutdoorBulletPuffs = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_blockng_gibs_bug),
            viewModel.fixBlockingGibsBugAsLiveData
        ) {
            viewModel.fixBlockingGibsBug = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fix_sound_propagation),
            viewModel.fixSoundPropagationAsLiveData) {
            viewModel.fixSoundPropagation = it
        }
    }

    @Composable
    private fun DrawTweaks (viewModel: PsyDoomComposeSettingsViewModel){
        DrawTitleText(stringResource(R.string.psydoom_tweaks))
        SwitchItem(
            stringResource(R.string.psydoom_extended_shoot_range),
            viewModel.useExtendedPlayerShootRangeAsLiveData) {
            viewModel.useExtendedPlayerShootRange = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_multimap_pickup),
            viewModel.allowMultiMapPickupAsLiveData) {
            viewModel.allowMultiMapPickup = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_input_latency_tweak),
            viewModel.useMoveInputLatencyTweakAsLiveData) {
            viewModel.useMoveInputLatencyTweak = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_ssg_delay_tweak),
            viewModel.useSuperShotgunDelayTweakAsLiveData) {
            viewModel.useSuperShotgunDelayTweak = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_allow_turning_cancel),
            viewModel.allowTurningCancellationAsLiveData) {
            viewModel.allowTurningCancellation = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_force_spawn_dm_things),
            viewModel.singlePlayerForceSpawnDmThingsAsLiveData) {
            viewModel.singlePlayerForceSpawnDmThings = it
        }
        DrawHorizontalDivider()

        val allowMovementStream = viewModel.allowMovementCancellationAsLiveData
            .getComposableValue(GameEnum.Always.value)

        val allowMovementCancellation by rememberSaveable(allowMovementStream) {
            mutableStateOf(GameEnum.fromValue(allowMovementStream))
        }

        ListPreferenceItem(stringResource(R.string.psydoom_allow_movement_cancel),
            allowMovementCancellation) {
            viewModel.allowMovementCancellation = it
        }
        DrawHorizontalDivider()

        val useFinalDoomPlayerMovementStream = viewModel.useFinalDoomPlayerMovementAsLiveData
            .getComposableValue(GameEnum.Always.value)

        val useFinalDoomPlayerMovement by rememberSaveable(useFinalDoomPlayerMovementStream) {
            mutableStateOf(GameEnum.fromValue(useFinalDoomPlayerMovementStream))
        }

        ListPreferenceItem(stringResource(R.string.psydoom_final_doom_player_physx),
            useFinalDoomPlayerMovement) {
            viewModel.useFinalDoomPlayerMovement = it
        }
        DrawHorizontalDivider()
        EditTextItem(
            stringResource(R.string.psydoom_lost_soul_spawn_limit),
            viewModel.lostSoulSpawnLimitAsLiveData, ) {
            viewModel.lostSoulSpawnLimit = it
        }
    }

    @Composable
    private fun DrawExtraGameSettings(viewModel: PsyDoomComposeSettingsViewModel){
        DrawTitleText(stringResource(R.string.psydoom_map_patches_to_apply))
        SwitchItem(
            stringResource(R.string.psydoom_gameplay_fixes),
            viewModel.enableMapPatchesGamePlayAsLiveData) {
            viewModel.enableMapPatchesGamePlay = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_visual_fixes),
            viewModel.enableMapPatchesVisualAsLiveData) {
            viewModel.enableMapPatchesVisual = it
        }
        DrawHorizontalDivider()
        SwitchItem(
            stringResource(R.string.psydoom_fixes),
            viewModel.enableMapPatchesPsyDoomAsLiveData) {
            viewModel.enableMapPatchesPsyDoom = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_game_tick_rate))

        val usePalTimingsStream = viewModel.usePalTimingsAsLiveData.getComposableValue(TickMode.NTSC.value)
        val usePalTimings by rememberSaveable(usePalTimingsStream) {
            mutableStateOf(TickMode.fromValue(usePalTimingsStream))}

        ListPreferenceItem(stringResource(R.string.psydoom_mode),
            usePalTimings ) {
            viewModel.usePalTimings = it
        }
        DrawHorizontalDivider()

        SwitchItem(stringResource(R.string.psydoom_demo_timings),
            viewModel.useDemoTimingsAsLiveData) {
            viewModel.useDemoTimings = it
        }
    }

    @Composable
    private fun DrawInputSettings(){
        val viewModel: PsyDoomComposeSettingsViewModel = koinViewModel()
        DrawTitleText(stringResource(R.string.psydoom_mouse))

        EditTextItem(stringResource(R.string.psydoom_mouse_turn_speed),
            viewModel.mouseTurnSpeedAsLiveData, ) {
            viewModel.mouseTurnSpeed = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_gamepad))

        EditTextItem(stringResource(R.string.psydoom_dead_zone),
            viewModel.gamepadDeadZoneAsLiveData, ) {
            viewModel.gamepadDeadZone = it
        }
        DrawHorizontalDivider()

        EditTextItem(stringResource(R.string.psydoom_high_fast_turn_speed),
            viewModel.gamepadFastTurnSpeedHighAsLiveData, ) {
            viewModel.gamepadFastTurnSpeedHigh = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_low_fast_turn_speed),
            viewModel.gamepadFastTurnSpeedLowAsLiveData, ) {
            viewModel.gamepadFastTurnSpeedLow = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_low_turn_speed),
            viewModel.gamepadTurnSpeedLowAsLiveData, ) {
            viewModel.gamepadTurnSpeedLow = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_high_turn_speed),
            viewModel.gamepadTurnSpeedHighAsLiveData, ) {
            viewModel.gamepadTurnSpeedHigh = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_analog_to_digital_threshold),
            viewModel.analogToDigitalThresholdAsLiveData, ) {
            viewModel.analogToDigitalThreshold = it
        }
        DrawHorizontalDivider()
    }

    @Composable
    private fun DrawAudioSettings(){
        val viewModel: PsyDoomComposeSettingsViewModel = koinViewModel()
        DrawTitleText(stringResource(R.string.psydoom_audio))

        EditTextItem(stringResource(R.string.psydoom_audio_buffer_size),
            viewModel.audioBufferSizeAsLiveData, ) {
            viewModel.audioBufferSize = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_spu_ram_size),
            viewModel.spuRamSizeAsLiveData, ) {
            viewModel.spuRamSize = it
        }
        DrawHorizontalDivider()
    }

    @Composable
    private fun DrawCheatsScreen(){
        val viewModel: PsyDoomComposeSettingsViewModel = koinViewModel()
        DrawTitleText(stringResource(R.string.psydoom_cheats_general))

        SwitchItem(stringResource(R.string.psydoom_developer_cheats_shortcuts),
            viewModel.enableDevCheatShortcutsAsLiveData) {
            viewModel.enableDevCheatShortcuts = it
        }
        DrawHorizontalDivider()
        SwitchItem(stringResource(R.string.psydoom_developer_map_reload_key),
            viewModel.enableDevInPlaceReloadFunctionKeyAsLiveData) {
            viewModel.enableDevInPlaceReloadFunctionKey = it
        }
        DrawHorizontalDivider()
        SwitchItem(stringResource(R.string.psydoom_developer_map_reload),
            viewModel.enableDevMapAutoReloadAsLiveData) {
            viewModel.enableDevMapAutoReload = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_cheat_key_sequences))
        EditTextItem(stringResource(R.string.psydoom_god_mode),
            viewModel.cheatKeySequenceGodModeAsLiveData, ) {
            viewModel.cheatKeySequenceGodMode = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_no_clip),
            viewModel.cheatKeySequenceNoClipAsLiveData, ) {
            viewModel.cheatKeySequenceNoClip = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_level_warp),
            viewModel.cheatKeySequenceLevelWarpAsLiveData, ) {
            viewModel.cheatKeySequenceLevelWarp = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_weapons_keys_armor_cheat),
            viewModel.cheatKeySequenceWeaponsKeysAndArmorAsLiveData, ) {
            viewModel.cheatKeySequenceWeaponsKeysAndArmor = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_weapons_armor_cheat),
            viewModel.cheatKeySequenceWeaponsAndArmorAsLiveData, ) {
            viewModel.cheatKeySequenceWeaponsAndArmor = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_map_lines_on),
            viewModel.cheatKeySequenceAllMapLinesOnAsLiveData, ) {
            viewModel.cheatKeySequenceAllMapLinesOn = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_map_things_on),
            viewModel.cheatKeySequenceAllMapThingsOnAsLiveData, ) {
            viewModel.cheatKeySequenceAllMapThingsOn = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_xray_vision),
            viewModel.cheatKeySequenceXRayVisionAsLiveData, ) {
            viewModel.cheatKeySequenceXRayVision = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_vram_viewer),
            viewModel.cheatKeySequenceVramViewerAsLiveData, ) {
            viewModel.cheatKeySequenceVramViewer = it
        }
        DrawHorizontalDivider()
        EditTextItem(stringResource(R.string.psydoom_no_target),
            viewModel.cheatKeySequenceNoTargetAsLiveData, ) {
            viewModel.cheatKeySequenceNoTarget = it
        }
        DrawHorizontalDivider()
    }

    @Composable
    private fun DrawMultiplayerScreen(){
        val viewModel: PsyDoomComposeSettingsViewModel = koinViewModel()
        DrawTitleText(stringResource(R.string.psydoom_cooperative))

        SwitchItem(stringResource(R.string.psydoom_no_friendly_fire),
            viewModel.coopNoFriendlyFireAsLiveData) {
            viewModel.coopNoFriendlyFire = it
        }
        DrawHorizontalDivider()

        SwitchItem(stringResource(R.string.psydoom_spawn_deathmatch_only_things),
            viewModel.coopForceSpawnDeathmatchThingsAsLiveData) {
            viewModel.coopForceSpawnDeathmatchThings = it
        }
        DrawHorizontalDivider()

        SwitchItem(stringResource(R.string.psydoom_preserve_keys_on_respawn),
            viewModel.coopPreserveKeysAsLiveData) {
            viewModel.coopPreserveKeys = it
        }
        DrawHorizontalDivider()
        SwitchItem(stringResource(R.string.psydoom_preserve_weapons_on_respawn),
            viewModel.coopPreserveWeaponsAsLiveData) {
            viewModel.coopPreserveWeapons = it
        }
        DrawHorizontalDivider()

        val coopPreserveAmmoFactorAsLiveStream = viewModel.coopPreserveAmmoFactorAsLiveData
            .getComposableValue(RespawnAmmoEnum.HALF.value)
        val coopPreserveAmmoFactor by rememberSaveable(coopPreserveAmmoFactorAsLiveStream) {
            mutableStateOf(RespawnAmmoEnum.fromValue(coopPreserveAmmoFactorAsLiveStream))}

        ListPreferenceItem(stringResource(R.string.psydoom_preserve_ammo_on_respawn),
            coopPreserveAmmoFactor,) {
            viewModel.coopPreserveAmmoFactor = it
        }
        DrawHorizontalDivider()

        SwitchItem(stringResource(R.string.psydoom_preserve_weapons_on_respawn),
            viewModel.coopPreserveWeaponsAsLiveData) {
            viewModel.coopPreserveWeapons = it
        }
        DrawHorizontalDivider()

        DrawTitleText(stringResource(R.string.psydoom_deathmatch))

        EditTextItem(stringResource(R.string.psydoom_frag_limit),
            viewModel.dmFragLimitAsLiveData, ) {
            viewModel.dmFragLimit = it
        }

        DrawHorizontalDivider()
        SwitchItem(stringResource(R.string.psydoom_disable_exits),
            viewModel.dmExitDisabledAsLiveData) {
            viewModel.dmExitDisabled = it
        }
        DrawHorizontalDivider()
        SwitchItem(stringResource(R.string.psydoom_auto_activate_boss),
            viewModel.dmActivateBossSpecialSectorsAsLiveData) {
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
            val stringEntries = entries.map { it.toString() }.toList()

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
            val stringEntries = entries.map { it.toString() }.toList()

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
            val stringEntries = entries.map { it.toString() }.toList()

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





