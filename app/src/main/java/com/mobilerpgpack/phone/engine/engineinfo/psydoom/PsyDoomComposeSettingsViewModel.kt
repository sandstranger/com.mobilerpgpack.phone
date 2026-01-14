package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.utils.ModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.PsyDoomModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.viewmodel.IniViewModel
import com.mobilerpgpack.phone.utils.Ini
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class PsyDoomComposeSettingsViewModel : IniViewModel() {

    private val pathToPsyDoomConfigsFolder = "${pathToRootUserFolder}${File.separator}" +
            "com.codelobster${File.separator}PsyDoom"

    private val graphicsIniFile =
        Ini("${pathToPsyDoomConfigsFolder}${File.separator}graphics_cfg.ini")

    private val gameIniFile = Ini("${pathToPsyDoomConfigsFolder}${File.separator}game_cfg.ini")

    private val inputIniFile = Ini("${pathToPsyDoomConfigsFolder}${File.separator}input_cfg.ini")

    private val audioIniFile = Ini("${pathToPsyDoomConfigsFolder}${File.separator}audio_cfg.ini")

    private val cheatsIniFile = Ini("${pathToPsyDoomConfigsFolder}${File.separator}cheats_cfg.ini")

    private val multiPlayerIniFile =
        Ini("${pathToPsyDoomConfigsFolder}${File.separator}multiplayer_cfg.ini")

    val modsModel : ModsModel by inject (named(EngineTypes.PsyDoom.toString()))

    val enableVsyncAsLiveData = graphicsIniFile.getBooleanValueFromInt("EnableVSync")
    val useExtendedAutomapColorsAsLiveData = graphicsIniFile.getBooleanValueFromInt("UseExtendedAutomapColors")
    val vulkanPixelsStretchAsLiveData = graphicsIniFile.getBooleanValueFromInt("VulkanPixelStretch")
    val widescreenEnabledAsLiveData = graphicsIniFile.getBooleanValueFromInt("VulkanWidescreenEnabled")
    val drawExtendedStatusBarAsLiveData = graphicsIniFile.getBooleanValueFromInt("VulkanDrawExtendedStatusBar")
    val disableVulkanRenderAsLiveData = graphicsIniFile.getBooleanValueFromInt("DisableVulkanRenderer")
    val enhanceWallDrawPrecisionAsLiveData = graphicsIniFile.getBooleanValueFromInt("EnhanceWallDrawPrecision")
    val skyLeakFixAsLiveData = graphicsIniFile.getBooleanValueFromInt("SkyLeakFix")
    val floorGapRenderFixAsLiveData = graphicsIniFile.getBooleanValueFromInt("FloorRenderGapFix")
    val tripleBufferAsLiveData = graphicsIniFile.getBooleanValueFromInt("VulkanTripleBuffer")
    val brightenAutomapAsLiveData = graphicsIniFile.getBooleanValueFromInt("VulkanBrightenAutomap")
    val use32bitShadingAsLiveData = graphicsIniFile.getBooleanValueFromInt("UseVulkan32BitShading")
    val topOverscanPixelsAsLiveData = graphicsIniFile.getIntValue("TopOverscanPixels")
    val renderHeightAsLiveData = graphicsIniFile.getIntValue("VulkanRenderHeight")
    val outputRefreshRateAsLiveData = graphicsIniFile.getIntValue("OutputRefreshRate")
    val antialiasingMultisamplesAsLiveData = graphicsIniFile.getIntValue("AntiAliasingMultisamples")

    val vramSizeInMbytesAsLiveData = graphicsIniFile.getIntValue("VramSizeInMegabytes")
    val bottomOverscanPixelsAsLiveData = graphicsIniFile.getIntValue("BottomOverscanPixels")
    val logicalDisplayWidthAsLiveData = graphicsIniFile.getIntValue("LogicalDisplayWidth")

    val interpolateSectorsAsLiveData = gameIniFile.getBooleanValueFromInt("InterpolateSectors")
    val interpolateMonstersAsLiveData = gameIniFile.getBooleanValueFromInt("InterpolateMonsters")
    val interpolateThingsAsLiveData = gameIniFile.getBooleanValueFromInt("InterpolateMobj")
    val interpolateWeaponAsLiveData = gameIniFile.getBooleanValueFromInt("InterpolateWeapon")

    val enableLevelTimerAsLiveData = gameIniFile.getBooleanValueFromInt("EnableSinglePlayerLevelTimer")
    val showPerfCountersAsLiveData = gameIniFile.getBooleanValueFromInt("ShowPerfCounters")
    val pauseOnFocusLostAsLiveData = gameIniFile.getBooleanValueFromInt("PauseOnWindowFocusLost")

    val fixLineActivationAsLiveData = gameIniFile.getBooleanValueFromInt("FixLineActivation")
    val itemPickupFixAsLiveData = gameIniFile.getBooleanValueFromInt("UseItemPickupFix")
    val fixMultiLineCrossingAsLiveData = gameIniFile.getBooleanValueFromInt("FixMultiLineSpecialCrossing")
    val fixKillCountAsLiveData = gameIniFile.getBooleanValueFromInt("FixKillCount")
    val playerRocketBlastFixAsLiveData = gameIniFile.getBooleanValueFromInt("UsePlayerRocketBlastFix")
    val fixSpriteVerticalWarpAsLiveData = gameIniFile.getBooleanValueFromInt("FixSpriteVerticalWarp")
    val fixViewBobStrengthAsLiveData = gameIniFile.getBooleanValueFromInt("FixViewBobStrength")
    val fixGravityStrengthAsLiveData = gameIniFile.getBooleanValueFromInt("FixGravityStrength")

    val useLostSoulSpawnFixAsLiveData = gameIniFile.getBooleanValueFromInt("UseLostSoulSpawnFix")
    val useLineOfSightOverflowFixAsLiveData = gameIniFile.getBooleanValueFromInt("UseLineOfSightOverflowFix")
    val fixOutdoorBulletPuffsAsLiveData = gameIniFile.getBooleanValueFromInt("FixOutdoorBulletPuffs")
    val fixBlockingGibsBugAsLiveData = gameIniFile.getBooleanValueFromInt("FixBlockingGibsBug")
    val useExtendedPlayerShootRangeAsLiveData = gameIniFile.getBooleanValueFromInt("UseExtendedPlayerShootRange")
    val allowMultiMapPickupAsLiveData = gameIniFile.getBooleanValueFromInt("AllowMultiMapPickup")
    val useMoveInputLatencyTweakAsLiveData = gameIniFile.getBooleanValueFromInt("UseMoveInputLatencyTweak")
    val useSuperShotgunDelayTweakAsLiveData = gameIniFile.getBooleanValueFromInt("UseSuperShotgunDelayTweak")
    val singlePlayerForceSpawnDmThingsAsLiveData = gameIniFile.getBooleanValueFromInt("SinglePlayerForceSpawnDmThings")
    val allowTurningCancellationAsLiveData = gameIniFile.getBooleanValueFromInt("AllowTurningCancellation")

    val allowMovementCancellationAsLiveData = gameIniFile.getIntValue("AllowMovementCancellation")
    val useFinalDoomPlayerMovementAsLiveData = gameIniFile.getIntValue("UseFinalDoomPlayerMovement")
    val usePalTimingsAsLiveData = gameIniFile.getIntValue("UsePalTimings")

    val fixSoundPropagationAsLiveData = gameIniFile.getBooleanValueFromInt("FixSoundPropagation")
    val useDemoTimingsAsLiveData = gameIniFile.getBooleanValueFromInt("UseDemoTimings")

    val enableMapPatchesGamePlayAsLiveData = gameIniFile.getBooleanValueFromInt("EnableMapPatches_GamePlay")
    val enableMapPatchesVisualAsLiveData = gameIniFile.getBooleanValueFromInt("EnableMapPatches_Visual")
    val enableMapPatchesPsyDoomAsLiveData = gameIniFile.getBooleanValueFromInt("EnableMapPatches_PsyDoom")

    val enableDevMapAutoReloadAsLiveData = cheatsIniFile.getBooleanValueFromInt("EnableDevMapAutoReload")
    val enableDevCheatShortcutsAsLiveData = cheatsIniFile.getBooleanValueFromInt("EnableDevCheatShortcuts")
    val enableDevInPlaceReloadFunctionKeyAsLiveData = cheatsIniFile.getBooleanValueFromInt("EnableDevInPlaceReloadFunctionKey")

    val coopNoFriendlyFireAsLiveData = multiPlayerIniFile.getBooleanValueFromInt("CoopNoFriendlyFire")
    val coopPreserveKeysAsLiveData = multiPlayerIniFile.getBooleanValueFromInt("CoopPreserveKeys")
    val coopPreserveWeaponsAsLiveData = multiPlayerIniFile.getBooleanValueFromInt("CoopPreserveWeapons")
    val coopForceSpawnDeathmatchThingsAsLiveData = multiPlayerIniFile.getBooleanValueFromInt("CoopForceSpawnDeathmatchThings")
    val dmActivateBossSpecialSectorsAsLiveData = multiPlayerIniFile.getBooleanValueFromInt("DmActivateBossSpecialSectors")
    val dmExitDisabledAsLiveData = multiPlayerIniFile.getBooleanValueFromInt("DmExitDisabled")

    val coopPreserveAmmoFactorAsLiveData = multiPlayerIniFile.getIntValue("CoopPreserveAmmoFactor")
    val dmFragLimitAsLiveData = multiPlayerIniFile.getIntValue("DmFragLimit")

    val cheatKeySequenceGodModeAsLiveData = cheatsIniFile.getStringValue("CheatKeySequence_GodMode")
    val cheatKeySequenceNoClipAsLiveData = cheatsIniFile.getStringValue("CheatKeySequence_NoClip")
    val cheatKeySequenceLevelWarpAsLiveData = cheatsIniFile.getStringValue("CheatKeySequence_LevelWarp")
    val cheatKeySequenceWeaponsKeysAndArmorAsLiveData = cheatsIniFile.getStringValue("CheatKeySequence_WeaponsKeysAndArmor")
    val cheatKeySequenceWeaponsAndArmorAsLiveData = cheatsIniFile.getStringValue("CheatKeySequence_WeaponsAndArmor")
    val cheatKeySequenceAllMapLinesOnAsLiveData = cheatsIniFile.getStringValue("CheatKeySequence_AllMapLinesOn")
    val cheatKeySequenceAllMapThingsOnAsLiveData = cheatsIniFile.getStringValue("CheatKeySequence_AllMapThingsOn")
    val cheatKeySequenceXRayVisionAsLiveData = cheatsIniFile.getStringValue("CheatKeySequence_XRayVision")
    val cheatKeySequenceVramViewerAsLiveData = cheatsIniFile.getStringValue("CheatKeySequence_VramViewer")
    val cheatKeySequenceNoTargetAsLiveData = cheatsIniFile.getStringValue("CheatKeySequence_NoTarget")

    val bobScaleAsLiveData = gameIniFile.getFloatValue("ViewBobbingStrength")
    val heapSizeAsLiveData = gameIniFile.getIntValue("MainMemoryHeapSize")
    val lostSoulSpawnLimitAsLiveData = gameIniFile.getIntValue("LostSoulSpawnLimit")
    val mouseTurnSpeedAsLiveData = inputIniFile.getIntValue("MouseTurnSpeed")
    val gamepadFastTurnSpeedHighAsLiveData = inputIniFile.getIntValue("GamepadFastTurnSpeed_High")
    val gamepadFastTurnSpeedLowAsLiveData = inputIniFile.getIntValue("GamepadFastTurnSpeed_Low")
    val gamepadTurnSpeedHighAsLiveData = inputIniFile.getIntValue("GamepadTurnSpeed_High")
    val gamepadTurnSpeedLowAsLiveData = inputIniFile.getIntValue("GamepadTurnSpeed_Low")
    val audioBufferSizeAsLiveData = audioIniFile.getIntValue("AudioBufferSize")
    val spuRamSizeAsLiveData = audioIniFile.getIntValue("SpuRamSize")
    val gamepadDeadZoneAsLiveData = inputIniFile.getFloatValue("GamepadDeadZone")
    val analogToDigitalThresholdAsLiveData = inputIniFile.getFloatValue("AnalogToDigitalThreshold")

    // --- Свойства, использующие LiveData ---
    var enableVsync: Boolean
        get() = enableVsyncAsLiveData.value!!
        set(value) = graphicsIniFile.setValueAsInt("EnableVSync", value)

    var useExtendedAutomapColors: Boolean
        get() = useExtendedAutomapColorsAsLiveData.value!!
        set(value) = graphicsIniFile.setValueAsInt("UseExtendedAutomapColors", value)

    var vulkanPixelsStretch: Boolean
        get() = vulkanPixelsStretchAsLiveData.value!!
        set(value) = graphicsIniFile.setValueAsInt("VulkanPixelStretch", value)

    var widescreenEnabled: Boolean
        get() = widescreenEnabledAsLiveData.value!!
        set(value) = graphicsIniFile.setValueAsInt("VulkanWidescreenEnabled", value)

    var drawExtendedStatusBar: Boolean
        get() = drawExtendedStatusBarAsLiveData.value!!
        set(value) = graphicsIniFile.setValueAsInt("VulkanDrawExtendedStatusBar", value)

    var disableVulkanRender: Boolean
        get() = disableVulkanRenderAsLiveData.value!!
        set(value) = graphicsIniFile.setValueAsInt("DisableVulkanRenderer", value)

    var enhanceWallDrawPrecision: Boolean
        get() = enhanceWallDrawPrecisionAsLiveData.value!!
        set(value) = graphicsIniFile.setValueAsInt("EnhanceWallDrawPrecision", value)

    var skyLeakFix: Boolean
        get() = skyLeakFixAsLiveData.value!!
        set(value) = graphicsIniFile.setValueAsInt("SkyLeakFix", value)

    var floorGapRenderFix: Boolean
        get() = floorGapRenderFixAsLiveData.value!!
        set(value) = graphicsIniFile.setValueAsInt("FloorRenderGapFix", value)

    var tripleBuffer: Boolean
        get() = tripleBufferAsLiveData.value!!
        set(value) = graphicsIniFile.setValueAsInt("VulkanTripleBuffer", value)

    var brightenAutomap: Boolean
        get() = brightenAutomapAsLiveData.value!!
        set(value) = graphicsIniFile.setValueAsInt("VulkanBrightenAutomap", value)

    var use32bitShading: Boolean
        get() = use32bitShadingAsLiveData.value!!
        set(value) = graphicsIniFile.setValueAsInt("UseVulkan32BitShading", value)

    var topOverscanPixels: Int
        get() = topOverscanPixelsAsLiveData.value!!
        set(value) = graphicsIniFile.setValue("TopOverscanPixels", value)

    var renderHeight: Int
        get() = renderHeightAsLiveData.value!!
        set(value) = graphicsIniFile.setValue("VulkanRenderHeight", value)

    var outputRefreshRate: Int
        get() = outputRefreshRateAsLiveData.value!!
        set(value) = graphicsIniFile.setValue("OutputRefreshRate", value)

    var antialiasingMultisamples: Int
        get() = antialiasingMultisamplesAsLiveData.value!!
        set(value) = graphicsIniFile.setValue("AntiAliasingMultisamples", value)

    var vramSizeInMbytes: Int
        get() = vramSizeInMbytesAsLiveData.value!!
        set(value) = graphicsIniFile.setValue("VramSizeInMegabytes", value)

    var bottomOverscanPixels: Int
        get() = bottomOverscanPixelsAsLiveData.value!!
        set(value) = graphicsIniFile.setValue("BottomOverscanPixels", value)

    var logicalDisplayWidth: Int
        get() = logicalDisplayWidthAsLiveData.value!!
        set(value) = graphicsIniFile.setValue("LogicalDisplayWidth", value)

    var interpolateSectors: Boolean
        get() = interpolateSectorsAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("InterpolateSectors", value)

    var interpolateMonsters: Boolean
        get() = interpolateMonstersAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("InterpolateMonsters", value)

    var interpolateThings: Boolean
        get() = interpolateThingsAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("InterpolateMobj", value)

    var interpolateWeapon: Boolean
        get() = interpolateWeaponAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("InterpolateWeapon", value)

    var enableLevelTimer: Boolean
        get() = enableLevelTimerAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("EnableSinglePlayerLevelTimer", value)

    var showPerfCounters: Boolean
        get() = showPerfCountersAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("ShowPerfCounters", value)

    var pauseOnFocusLost: Boolean
        get() = pauseOnFocusLostAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("PauseOnWindowFocusLost", value)

    var fixLineActivation: Boolean
        get() = fixLineActivationAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("FixLineActivation", value)

    var itemPickupFix: Boolean
        get() = itemPickupFixAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("UseItemPickupFix", value)

    var fixMultiLineCrossing: Boolean
        get() = fixMultiLineCrossingAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("FixMultiLineSpecialCrossing", value)

    var fixKillCount: Boolean
        get() = fixKillCountAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("FixKillCount", value)

    var playerRocketBlastFix: Boolean
        get() = playerRocketBlastFixAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("UsePlayerRocketBlastFix", value)

    var fixSpriteVerticalWarp: Boolean
        get() = fixSpriteVerticalWarpAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("FixSpriteVerticalWarp", value)

    var fixViewBobStrength: Boolean
        get() = fixViewBobStrengthAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("FixViewBobStrength", value)

    var fixGravityStrength: Boolean
        get() = fixGravityStrengthAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("FixGravityStrength", value)

    var useLostSoulSpawnFix: Boolean
        get() = useLostSoulSpawnFixAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("UseLostSoulSpawnFix", value)

    var useLineOfSightOverflowFix: Boolean
        get() = useLineOfSightOverflowFixAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("UseLineOfSightOverflowFix", value)

    var fixOutdoorBulletPuffs: Boolean
        get() = fixOutdoorBulletPuffsAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("FixOutdoorBulletPuffs", value)

    var fixBlockingGibsBug: Boolean
        get() = fixBlockingGibsBugAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("FixBlockingGibsBug", value)

    var useExtendedPlayerShootRange: Boolean
        get() = useExtendedPlayerShootRangeAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("UseExtendedPlayerShootRange", value)

    var allowMultiMapPickup: Boolean
        get() = allowMultiMapPickupAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("AllowMultiMapPickup", value)

    var useMoveInputLatencyTweak: Boolean
        get() = useMoveInputLatencyTweakAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("UseMoveInputLatencyTweak", value)

    var useSuperShotgunDelayTweak: Boolean
        get() = useSuperShotgunDelayTweakAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("UseSuperShotgunDelayTweak", value)

    var singlePlayerForceSpawnDmThings: Boolean
        get() = singlePlayerForceSpawnDmThingsAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("SinglePlayerForceSpawnDmThings", value)

    var allowTurningCancellation: Boolean
        get() = allowTurningCancellationAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("AllowTurningCancellation", value)

    var allowMovementCancellation: PsyDoomComposeSettings.GameEnum
        get() = PsyDoomComposeSettings.GameEnum.fromValue(allowMovementCancellationAsLiveData.value!!)!!
        set(value) = gameIniFile.setValue("AllowMovementCancellation", value.value)

    var useFinalDoomPlayerMovement: PsyDoomComposeSettings.GameEnum
        get() = PsyDoomComposeSettings.GameEnum.fromValue(useFinalDoomPlayerMovementAsLiveData.value!!)!!
        set(value) = gameIniFile.setValue("UseFinalDoomPlayerMovement", value.value)

    var usePalTimings: PsyDoomComposeSettings.TickMode
        get() = PsyDoomComposeSettings.TickMode.fromValue(usePalTimingsAsLiveData.value!!)!!
        set(value) = gameIniFile.setValue("UsePalTimings", value.value)

    var fixSoundPropagation: Boolean
        get() = fixSoundPropagationAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("FixSoundPropagation", value)

    var useDemoTimings: Boolean
        get() = useDemoTimingsAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("UseDemoTimings", value)

    var enableMapPatchesGamePlay: Boolean
        get() = enableMapPatchesGamePlayAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("EnableMapPatches_GamePlay", value)

    var enableMapPatchesVisual: Boolean
        get() = enableMapPatchesVisualAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("EnableMapPatches_Visual", value)

    var enableMapPatchesPsyDoom: Boolean
        get() = enableMapPatchesPsyDoomAsLiveData.value!!
        set(value) = gameIniFile.setValueAsInt("EnableMapPatches_PsyDoom", value)

    var enableDevMapAutoReload: Boolean
        get() = enableDevMapAutoReloadAsLiveData.value!!
        set(value) = cheatsIniFile.setValueAsInt("EnableDevMapAutoReload", value)

    var enableDevCheatShortcuts: Boolean
        get() = enableDevCheatShortcutsAsLiveData.value!!
        set(value) = cheatsIniFile.setValueAsInt("EnableDevCheatShortcuts", value)

    var enableDevInPlaceReloadFunctionKey: Boolean
        get() = enableDevInPlaceReloadFunctionKeyAsLiveData.value!!
        set(value) = cheatsIniFile.setValueAsInt("EnableDevInPlaceReloadFunctionKey", value)

    var coopNoFriendlyFire: Boolean
        get() = coopNoFriendlyFireAsLiveData.value!!
        set(value) = multiPlayerIniFile.setValueAsInt("CoopNoFriendlyFire", value)

    var coopPreserveKeys: Boolean
        get() = coopPreserveKeysAsLiveData.value!!
        set(value) = multiPlayerIniFile.setValueAsInt("CoopPreserveKeys", value)

    var coopPreserveWeapons: Boolean
        get() = coopPreserveWeaponsAsLiveData.value!!
        set(value) = multiPlayerIniFile.setValueAsInt("CoopPreserveWeapons", value)

    var coopForceSpawnDeathmatchThings: Boolean
        get() = coopForceSpawnDeathmatchThingsAsLiveData.value!!
        set(value) = multiPlayerIniFile.setValueAsInt("CoopForceSpawnDeathmatchThings", value)

    var dmActivateBossSpecialSectors: Boolean
        get() = dmActivateBossSpecialSectorsAsLiveData.value!!
        set(value) = multiPlayerIniFile.setValueAsInt("DmActivateBossSpecialSectors", value)

    var dmExitDisabled: Boolean
        get() = dmExitDisabledAsLiveData.value!!
        set(value) = multiPlayerIniFile.setValueAsInt("DmExitDisabled", value)

    var coopPreserveAmmoFactor: PsyDoomComposeSettings.RespawnAmmoEnum
        get() = PsyDoomComposeSettings.RespawnAmmoEnum.fromValue(coopPreserveAmmoFactorAsLiveData.value!!)!!
        set(value) = multiPlayerIniFile.setValue("CoopPreserveAmmoFactor", value.value)

    var dmFragLimit: Int
        get() = dmFragLimitAsLiveData.value!!.coerceAtLeast(0)
        set(value) = multiPlayerIniFile.setValue("DmFragLimit", value.coerceAtLeast(0))

    var cheatKeySequenceGodMode: String
        get() = cheatKeySequenceGodModeAsLiveData.value!!
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_GodMode", value)
            }
        }

    var cheatKeySequenceNoClip: String
        get() = cheatKeySequenceNoClipAsLiveData.value!!
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_NoClip", value)
            }
        }

    var cheatKeySequenceLevelWarp: String
        get() = cheatKeySequenceLevelWarpAsLiveData.value!!
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_LevelWarp", value)
            }
        }

    var cheatKeySequenceWeaponsKeysAndArmor: String
        get() = cheatKeySequenceWeaponsKeysAndArmorAsLiveData.value!!
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_WeaponsKeysAndArmor", value)
            }
        }

    var cheatKeySequenceWeaponsAndArmor: String
        get() = cheatKeySequenceWeaponsAndArmorAsLiveData.value!!
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_WeaponsAndArmor", value)
            }
        }

    var cheatKeySequenceAllMapLinesOn: String
        get() = cheatKeySequenceAllMapLinesOnAsLiveData.value!!
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_AllMapLinesOn", value)
            }
        }

    var cheatKeySequenceAllMapThingsOn: String
        get() = cheatKeySequenceAllMapThingsOnAsLiveData.value!!
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_AllMapThingsOn", value)
            }
        }

    var cheatKeySequenceXRayVision: String
        get() = cheatKeySequenceXRayVisionAsLiveData.value!!
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_XRayVision", value)
            }
        }

    var cheatKeySequenceVramViewer: String
        get() = cheatKeySequenceVramViewerAsLiveData.value!!
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_VramViewer", value)
            }
        }

    var cheatKeySequenceNoTarget: String
        get() = cheatKeySequenceNoTargetAsLiveData.value!!
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_NoTarget", value)
            }
        }

    var bobScale: Float
        get() = bobScaleAsLiveData.value!!.coerceAtLeast(0.0f)
        set(value) = gameIniFile.setValue("ViewBobbingStrength", value.coerceAtLeast(0.0f))

    var heapSize: Int
        get() = heapSizeAsLiveData.value!!
        set(value) = gameIniFile.setValue("MainMemoryHeapSize", value)

    var lostSoulSpawnLimit: Int
        get() = lostSoulSpawnLimitAsLiveData.value!!
        set(value) = gameIniFile.setValue("LostSoulSpawnLimit", value)

    var mouseTurnSpeed: Int
        get() = mouseTurnSpeedAsLiveData.value!!.coerceAtLeast(0)
        set(value) = inputIniFile.setValue("MouseTurnSpeed", value.coerceAtLeast(0))

    var gamepadFastTurnSpeedHigh: Int
        get() = gamepadFastTurnSpeedHighAsLiveData.value!!.coerceAtLeast(0)
        set(value) = inputIniFile.setValue("GamepadFastTurnSpeed_High", value.coerceAtLeast(0))

    var gamepadFastTurnSpeedLow: Int
        get() = gamepadFastTurnSpeedLowAsLiveData.value!!.coerceAtLeast(0)
        set(value) = inputIniFile.setValue("GamepadFastTurnSpeed_Low", value.coerceAtLeast(0))

    var gamepadTurnSpeedHigh: Int
        get() = gamepadTurnSpeedHighAsLiveData.value!!.coerceAtLeast(0)
        set(value) = inputIniFile.setValue("GamepadTurnSpeed_High", value.coerceAtLeast(0))

    var gamepadTurnSpeedLow: Int
        get() = gamepadTurnSpeedLowAsLiveData.value!!.coerceAtLeast(0)
        set(value) = inputIniFile.setValue("GamepadTurnSpeed_Low", value.coerceAtLeast(0))

    var audioBufferSize: Int
        get() = audioBufferSizeAsLiveData.value!!.coerceAtLeast(0)
        set(value) = audioIniFile.setValue("AudioBufferSize", value.coerceAtLeast(0))

    var spuRamSize: Int
        get() = spuRamSizeAsLiveData.value!!
        set(value) = audioIniFile.setValue("SpuRamSize", value)

    var gamepadDeadZone: Float
        get() = gamepadDeadZoneAsLiveData.value!!.coerceIn(0.0f, 1.0f)
        set(value) = inputIniFile.setValue("GamepadDeadZone", value.coerceIn(0.0f, 1.0f))

    var analogToDigitalThreshold: Float
        get() = analogToDigitalThresholdAsLiveData.value!!.coerceIn(0.0f, 1.0f)
        set(value) = inputIniFile.setValue("AnalogToDigitalThreshold", value.coerceIn(0.0f, 1.0f))

    override fun unloadIniFiles(){
        super.unloadIniFiles()
        graphicsIniFile.clear()
        gameIniFile.clear()
        inputIniFile.clear()
        audioIniFile.clear()
        cheatsIniFile.clear()
        multiPlayerIniFile.clear()
    }

    override fun reloadIniFiles (){
        graphicsIniFile.load()
        gameIniFile.load()
        inputIniFile.load()
        audioIniFile.load()
        cheatsIniFile.load()
        multiPlayerIniFile.load()
        super.reloadIniFiles()
    }
}