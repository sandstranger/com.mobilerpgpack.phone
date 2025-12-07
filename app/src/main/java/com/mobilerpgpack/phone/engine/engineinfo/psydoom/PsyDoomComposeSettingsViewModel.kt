package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import com.mobilerpgpack.phone.engine.engineinfo.utils.PsyDoomModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.viewmodel.IniViewModel
import com.mobilerpgpack.phone.utils.Ini
import org.koin.core.component.inject
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

    val modsModel : PsyDoomModsModel by inject ()

    var enableVsync : Boolean
        get() = graphicsIniFile.getBooleanValueFromInt("EnableVSync")
        set(value) = graphicsIniFile.setValueAsInt("EnableVSync", value)

    var useExtendedAutomapColors : Boolean
        get() = graphicsIniFile.getBooleanValueFromInt("UseExtendedAutomapColors")
        set(value) = graphicsIniFile.setValueAsInt("UseExtendedAutomapColors", value)

    var vulkanPixelsStretch : Boolean
        get() = graphicsIniFile.getBooleanValueFromInt("VulkanPixelStretch")
        set(value) = graphicsIniFile.setValueAsInt("VulkanPixelStretch", value)

    var widescreenEnabled : Boolean
        get() = graphicsIniFile.getBooleanValueFromInt("VulkanWidescreenEnabled")
        set(value) = graphicsIniFile.setValueAsInt("VulkanWidescreenEnabled", value)

    var drawExtendedStatusBar : Boolean
        get() = graphicsIniFile.getBooleanValueFromInt("VulkanDrawExtendedStatusBar")
        set(value) = graphicsIniFile.setValueAsInt("VulkanDrawExtendedStatusBar", value)

    var disableVulkanRender : Boolean
        get() = graphicsIniFile.getBooleanValueFromInt("DisableVulkanRenderer")
        set(value) = graphicsIniFile.setValueAsInt("DisableVulkanRenderer", value)

    var enhanceWallDrawPrecision : Boolean
        get() = graphicsIniFile.getBooleanValueFromInt("EnhanceWallDrawPrecision")
        set(value) = graphicsIniFile.setValueAsInt("EnhanceWallDrawPrecision", value)

    var skyLeakFix : Boolean
        get() = graphicsIniFile.getBooleanValueFromInt("SkyLeakFix")
        set(value) = graphicsIniFile.setValueAsInt("SkyLeakFix", value)

    var floorGapRenderFix : Boolean
        get() = graphicsIniFile.getBooleanValueFromInt("FloorRenderGapFix")
        set(value) = graphicsIniFile.setValueAsInt("FloorRenderGapFix", value)

    var tripleBuffer : Boolean
        get() = graphicsIniFile.getBooleanValueFromInt("VulkanTripleBuffer")
        set(value) = graphicsIniFile.setValueAsInt("VulkanTripleBuffer", value)

    var brightenAutomap : Boolean
        get() = graphicsIniFile.getBooleanValueFromInt("VulkanBrightenAutomap")
        set(value) = graphicsIniFile.setValueAsInt("VulkanBrightenAutomap", value)

    var use32bitShading : Boolean
        get() = graphicsIniFile.getBooleanValueFromInt("UseVulkan32BitShading")
        set(value) = graphicsIniFile.setValueAsInt("UseVulkan32BitShading", value)

    var topOverscanPixels : Int
        get() = graphicsIniFile.getIntValue("TopOverscanPixels")
        set(value) = graphicsIniFile.setValue("TopOverscanPixels", value)

    var renderHeight : Int
        get() = graphicsIniFile.getIntValue("VulkanRenderHeight")
        set(value) = graphicsIniFile.setValue("VulkanRenderHeight", value)

    var outputRefreshRate : Int
        get() = graphicsIniFile.getIntValue("OutputRefreshRate")
        set(value) = graphicsIniFile.setValue("OutputRefreshRate", value)

    var antialiasingMultisamples : Int
        get() = graphicsIniFile.getIntValue("AntiAliasingMultisamples")
        set(value) = graphicsIniFile.setValue("AntiAliasingMultisamples", value)

    var vramSizeInMbytes : Int
        get() = graphicsIniFile.getIntValue("VramSizeInMegabytes")
        set(value) = graphicsIniFile.setValue("VramSizeInMegabytes", value)

    var bottomOverscanPixels : Int
        get() = graphicsIniFile.getIntValue("BottomOverscanPixels")
        set(value) = graphicsIniFile.setValue("BottomOverscanPixels", value)

    var logicalDisplayWidth : Int
        get() = graphicsIniFile.getIntValue("LogicalDisplayWidth")
        set(value) = graphicsIniFile.setValue("LogicalDisplayWidth", value)

    var interpolateSectors : Boolean
        get() = gameIniFile.getBooleanValueFromInt("InterpolateSectors")
        set(value) = gameIniFile.setValueAsInt("InterpolateSectors", value)

    var interpolateMonsters : Boolean
        get() = gameIniFile.getBooleanValueFromInt("InterpolateMonsters")
        set(value) = gameIniFile.setValueAsInt("InterpolateMonsters", value)

    var interpolateThings : Boolean
        get() = gameIniFile.getBooleanValueFromInt("InterpolateMobj")
        set(value) = gameIniFile.setValueAsInt("InterpolateMobj", value)

    var interpolateWeapon : Boolean
        get() = gameIniFile.getBooleanValueFromInt("InterpolateWeapon")
        set(value) = gameIniFile.setValueAsInt("InterpolateWeapon", value)

    var enableLevelTimer : Boolean
        get() = gameIniFile.getBooleanValueFromInt("EnableSinglePlayerLevelTimer")
        set(value) = gameIniFile.setValueAsInt("EnableSinglePlayerLevelTimer", value)

    var showPerfCounters : Boolean
        get() = gameIniFile.getBooleanValueFromInt("ShowPerfCounters")
        set(value) = gameIniFile.setValueAsInt("ShowPerfCounters", value)

    var pauseOnFocusLost : Boolean
        get() = gameIniFile.getBooleanValueFromInt("PauseOnWindowFocusLost")
        set(value) = gameIniFile.setValueAsInt("PauseOnWindowFocusLost",value)

    var fixLineActivation : Boolean
        get() = gameIniFile.getBooleanValueFromInt("FixLineActivation")
        set(value) = gameIniFile.setValueAsInt("FixLineActivation", value)

    var itemPickupFix : Boolean
        get() = gameIniFile.getBooleanValueFromInt("UseItemPickupFix")
        set(value) = gameIniFile.setValueAsInt("UseItemPickupFix", value)

    var fixMultiLineCrossing : Boolean
        get() = gameIniFile.getBooleanValueFromInt("FixMultiLineSpecialCrossing")
        set(value) = gameIniFile.setValueAsInt("FixMultiLineSpecialCrossing", value)

    var fixKillCount : Boolean
        get() = gameIniFile.getBooleanValueFromInt("FixKillCount")
        set(value) = gameIniFile.setValueAsInt("FixKillCount", value)

    var playerRocketBlastFix : Boolean
        get() = gameIniFile.getBooleanValueFromInt("UsePlayerRocketBlastFix")
        set(value) = gameIniFile.setValueAsInt("UsePlayerRocketBlastFix", value)

    var fixSpriteVerticalWarp : Boolean
        get() = gameIniFile.getBooleanValueFromInt("FixSpriteVerticalWarp")
        set(value) = gameIniFile.setValueAsInt("FixSpriteVerticalWarp", value)

    var fixViewBobStrength : Boolean
        get() = gameIniFile.getBooleanValueFromInt("FixViewBobStrength")
        set(value) = gameIniFile.setValueAsInt("FixViewBobStrength", value)

    var fixGravityStrength : Boolean
        get() = gameIniFile.getBooleanValueFromInt("FixGravityStrength")
        set(value) = gameIniFile.setValueAsInt("FixGravityStrength", value)

    var useLostSoulSpawnFix : Boolean
        get() = gameIniFile.getBooleanValueFromInt("UseLostSoulSpawnFix")
        set(value) = gameIniFile.setValueAsInt("UseLostSoulSpawnFix", value)

    var useLineOfSightOverflowFix : Boolean
        get() = gameIniFile.getBooleanValueFromInt("UseLineOfSightOverflowFix")
        set(value) = gameIniFile.setValueAsInt("UseLineOfSightOverflowFix", value)

    var fixOutdoorBulletPuffs : Boolean
        get() = gameIniFile.getBooleanValueFromInt("FixOutdoorBulletPuffs")
        set(value) = gameIniFile.setValueAsInt("FixOutdoorBulletPuffs", value)

    var fixBlockingGibsBug : Boolean
        get() = gameIniFile.getBooleanValueFromInt("FixBlockingGibsBug")
        set(value) = gameIniFile.setValueAsInt("FixBlockingGibsBug", value)

    var useExtendedPlayerShootRange : Boolean
        get() = gameIniFile.getBooleanValueFromInt("UseExtendedPlayerShootRange")
        set(value) = gameIniFile.setValueAsInt("UseExtendedPlayerShootRange", value)

    var allowMultiMapPickup : Boolean
        get() = gameIniFile.getBooleanValueFromInt("AllowMultiMapPickup")
        set(value) = gameIniFile.setValueAsInt("AllowMultiMapPickup", value)

    var useMoveInputLatencyTweak : Boolean
        get() = gameIniFile.getBooleanValueFromInt("UseMoveInputLatencyTweak")
        set(value) = gameIniFile.setValueAsInt("UseMoveInputLatencyTweak", value)

    var useSuperShotgunDelayTweak : Boolean
        get() = gameIniFile.getBooleanValueFromInt("UseSuperShotgunDelayTweak")
        set(value) = gameIniFile.setValueAsInt("UseSuperShotgunDelayTweak", value)

    var singlePlayerForceSpawnDmThings : Boolean
        get() = gameIniFile.getBooleanValueFromInt("SinglePlayerForceSpawnDmThings")
        set(value) = gameIniFile.setValueAsInt("SinglePlayerForceSpawnDmThings", value)

    var allowTurningCancellation : Boolean
        get() = gameIniFile.getBooleanValueFromInt("AllowTurningCancellation")
        set(value) = gameIniFile.setValueAsInt("AllowTurningCancellation", value)

    var allowMovementCancellation : PsyDoomComposeSettings.GameEnum
        get() = PsyDoomComposeSettings.GameEnum.fromValue(gameIniFile.getIntValue("AllowMovementCancellation"))!!
        set(value) = gameIniFile.setValue("AllowMovementCancellation", value.value)

    var useFinalDoomPlayerMovement : PsyDoomComposeSettings.GameEnum
        get() = PsyDoomComposeSettings.GameEnum.fromValue(gameIniFile.getIntValue("UseFinalDoomPlayerMovement"))!!
        set(value) = gameIniFile.setValue("UseFinalDoomPlayerMovement", value.value)

    var usePalTimings : PsyDoomComposeSettings.TickMode
        get() = PsyDoomComposeSettings.TickMode.fromValue(gameIniFile.getIntValue("UsePalTimings"))!!
        set(value) = gameIniFile.setValue("UsePalTimings", value.value)

    var fixSoundPropagation : Boolean
        get() = gameIniFile.getBooleanValueFromInt("FixSoundPropagation")
        set(value) = gameIniFile.setValueAsInt("FixSoundPropagation", value)

    var useDemoTimings : Boolean
        get() = gameIniFile.getBooleanValueFromInt("UseDemoTimings")
        set(value) = gameIniFile.setValueAsInt("UseDemoTimings", value)

    var enableMapPatchesGamePlay : Boolean
        get() = gameIniFile.getBooleanValueFromInt("EnableMapPatches_GamePlay")
        set(value) = gameIniFile.setValueAsInt("EnableMapPatches_GamePlay", value)

    var enableMapPatchesVisual : Boolean
        get() = gameIniFile.getBooleanValueFromInt("EnableMapPatches_Visual")
        set(value) = gameIniFile.setValueAsInt("EnableMapPatches_Visual", value)

    var enableMapPatchesPsyDoom : Boolean
        get() = gameIniFile.getBooleanValueFromInt("EnableMapPatches_PsyDoom")
        set(value) = gameIniFile.setValueAsInt("EnableMapPatches_PsyDoom", value)

    var enableDevMapAutoReload : Boolean
        get() = cheatsIniFile.getBooleanValueFromInt("EnableDevMapAutoReload")
        set(value) = cheatsIniFile.setValueAsInt("EnableDevMapAutoReload", value)

    var enableDevCheatShortcuts : Boolean
        get() = cheatsIniFile.getBooleanValueFromInt("EnableDevCheatShortcuts")
        set(value) = cheatsIniFile.setValueAsInt("EnableDevCheatShortcuts", value)

    var enableDevInPlaceReloadFunctionKey : Boolean
        get() = cheatsIniFile.getBooleanValueFromInt("EnableDevInPlaceReloadFunctionKey")
        set(value) = cheatsIniFile.setValueAsInt("EnableDevInPlaceReloadFunctionKey", value)

    var coopNoFriendlyFire : Boolean
        get() = multiPlayerIniFile.getBooleanValueFromInt("CoopNoFriendlyFire")
        set(value) = multiPlayerIniFile.setValueAsInt("CoopNoFriendlyFire", value)

    var coopPreserveKeys : Boolean
        get() = multiPlayerIniFile.getBooleanValueFromInt("CoopPreserveKeys")
        set(value) = multiPlayerIniFile.setValueAsInt("CoopPreserveKeys", value)

    var coopPreserveWeapons : Boolean
        get() = multiPlayerIniFile.getBooleanValueFromInt("CoopPreserveWeapons")
        set(value) = multiPlayerIniFile.setValueAsInt("CoopPreserveWeapons", value)

    var coopForceSpawnDeathmatchThings : Boolean
        get() = multiPlayerIniFile.getBooleanValueFromInt("CoopForceSpawnDeathmatchThings")
        set(value) = multiPlayerIniFile.setValueAsInt("CoopForceSpawnDeathmatchThings", value)

    var dmActivateBossSpecialSectors : Boolean
        get() = multiPlayerIniFile.getBooleanValueFromInt("DmActivateBossSpecialSectors")
        set(value) = multiPlayerIniFile.setValueAsInt("DmActivateBossSpecialSectors", value)

    var dmExitDisabled : Boolean
        get() = multiPlayerIniFile.getBooleanValueFromInt("DmExitDisabled")
        set(value) = multiPlayerIniFile.setValueAsInt("DmExitDisabled", value)

    var coopPreserveAmmoFactor : PsyDoomComposeSettings.RespawnAmmoEnum
        get() = PsyDoomComposeSettings.RespawnAmmoEnum.fromValue(multiPlayerIniFile.getIntValue("CoopPreserveAmmoFactor"))!!
        set(value) = multiPlayerIniFile.setValue("CoopPreserveAmmoFactor", value.value)

    var dmFragLimit : Int
        get() = multiPlayerIniFile.getIntValue("DmFragLimit").coerceAtLeast(0)
        set(value) = multiPlayerIniFile.setValue("DmFragLimit", value.coerceAtLeast(0))

    var cheatKeySequenceGodMode : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_GodMode")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()){
                cheatsIniFile.setValue("CheatKeySequence_GodMode", value)
            }
        }

    var cheatKeySequenceNoClip : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_NoClip")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_NoClip", value)
            }
        }

    var cheatKeySequenceLevelWarp : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_LevelWarp")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_LevelWarp", value)
            }
        }

    var cheatKeySequenceWeaponsKeysAndArmor : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_WeaponsKeysAndArmor")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_WeaponsKeysAndArmor", value)
            }
        }

    var cheatKeySequenceWeaponsAndArmor : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_WeaponsAndArmor")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()){
                cheatsIniFile.setValue("CheatKeySequence_WeaponsAndArmor", value)
            }
        }

    var cheatKeySequenceAllMapLinesOn : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_AllMapLinesOn")
        set(value)  {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_AllMapLinesOn", value)
            }
        }

    var cheatKeySequenceAllMapThingsOn : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_AllMapThingsOn")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_AllMapThingsOn", value)
            }
        }

    var cheatKeySequenceXRayVision : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_XRayVision")
        set(value)  {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_XRayVision", value)
            }
        }

    var cheatKeySequenceVramViewer : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_VramViewer")
        set(value)  {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_VramViewer", value)
            }
        }

    var cheatKeySequenceNoTarget : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_NoTarget")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setValue("CheatKeySequence_NoTarget", value)
            }
        }

    var bobScale : Float
        get() = gameIniFile.getFloatValue("ViewBobbingStrength").coerceAtLeast(0.0f)
        set(value) = gameIniFile.setValue("ViewBobbingStrength", value.coerceAtLeast(0.0f))

    var heapSize : Int
        get() = gameIniFile.getIntValue("MainMemoryHeapSize")
        set(value) = gameIniFile.setValue("MainMemoryHeapSize", value)

    var lostSoulSpawnLimit : Int
        get() = gameIniFile.getIntValue("LostSoulSpawnLimit")
        set(value) = gameIniFile.setValue("LostSoulSpawnLimit", value)

    var mouseTurnSpeed : Int
        get() = inputIniFile.getIntValue("MouseTurnSpeed").coerceAtLeast(0)
        set(value) = inputIniFile.setValue("MouseTurnSpeed", value.coerceAtLeast(0))

    var gamepadFastTurnSpeedHigh : Int
        get() = inputIniFile.getIntValue("GamepadFastTurnSpeed_High").coerceAtLeast(0)
        set(value) = inputIniFile.setValue("GamepadFastTurnSpeed_High", value.coerceAtLeast(0))

    var gamepadFastTurnSpeedLow : Int
        get() = inputIniFile.getIntValue("GamepadFastTurnSpeed_Low").coerceAtLeast(0)
        set(value) = inputIniFile.setValue("GamepadFastTurnSpeed_Low", value.coerceAtLeast(0))

    var gamepadTurnSpeedHigh : Int
        get() = inputIniFile.getIntValue("GamepadTurnSpeed_High").coerceAtLeast(0)
        set(value) = inputIniFile.setValue("GamepadTurnSpeed_High", value.coerceAtLeast(0))

    var gamepadTurnSpeedLow : Int
        get() = inputIniFile.getIntValue("GamepadTurnSpeed_Low").coerceAtLeast(0)
        set(value) = inputIniFile.setValue("GamepadTurnSpeed_Low", value.coerceAtLeast(0))

    var audioBufferSize : Int
        get() = audioIniFile.getIntValue("AudioBufferSize").coerceAtLeast(0)
        set(value) = audioIniFile.setValue("AudioBufferSize", value.coerceAtLeast(0))

    var spuRamSize : Int
        get() = audioIniFile.getIntValue("SpuRamSize")
        set(value) = audioIniFile.setValue("SpuRamSize",value)

    var gamepadDeadZone : Float
        get() = inputIniFile.getFloatValue("GamepadDeadZone").coerceIn(0.0f, 1.0f)
        set(value) = inputIniFile.setValue("GamepadDeadZone", value.coerceIn(0.0f, 1.0f))

    var analogToDigitalThreshold : Float
        get() = inputIniFile.getFloatValue("AnalogToDigitalThreshold").coerceIn(0.0f, 1.0f)
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