package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.viewmodel.IniViewModel
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.IAssetExtractor
import com.mobilerpgpack.phone.utils.Ini
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
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

    var enableVsync : Boolean
        get() = graphicsIniFile.getBooleanValue("EnableVSync")
        set(value) = graphicsIniFile.setValue("EnableVSync", value)

    var useExtendedAutomapColors : Boolean
        get() = graphicsIniFile.getBooleanValue("UseExtendedAutomapColors")
        set(value) = graphicsIniFile.setValue("UseExtendedAutomapColors", value)

    var vulkanPixelsStretch : Boolean
        get() = graphicsIniFile.getBooleanValue("VulkanPixelStretch")
        set(value) = graphicsIniFile.setValue("VulkanPixelStretch", value)

    var widescreenEnabled : Boolean
        get() = graphicsIniFile.getBooleanValue("VulkanWidescreenEnabled")
        set(value) = graphicsIniFile.setValue("VulkanWidescreenEnabled", value)

    var drawExtendedStatusBar : Boolean
        get() = graphicsIniFile.getBooleanValue("VulkanDrawExtendedStatusBar")
        set(value) = graphicsIniFile.setValue("VulkanDrawExtendedStatusBar", value)

    var disableVulkanRender : Boolean
        get() = graphicsIniFile.getBooleanValue("DisableVulkanRenderer")
        set(value) = graphicsIniFile.setValue("DisableVulkanRenderer", value)

    var enhanceWallDrawPrecision : Boolean
        get() = graphicsIniFile.getBooleanValue("EnhanceWallDrawPrecision")
        set(value) = graphicsIniFile.setValue("EnhanceWallDrawPrecision", value)

    var skyLeakFix : Boolean
        get() = graphicsIniFile.getBooleanValue("SkyLeakFix")
        set(value) = graphicsIniFile.setValue("SkyLeakFix", value)

    var floorGapRenderFix : Boolean
        get() = graphicsIniFile.getBooleanValue("FloorRenderGapFix")
        set(value) = graphicsIniFile.setValue("FloorRenderGapFix", value)

    var tripleBuffer : Boolean
        get() = graphicsIniFile.getBooleanValue("VulkanTripleBuffer")
        set(value) = graphicsIniFile.setValue("VulkanTripleBuffer", value)

    var brightenAutomap : Boolean
        get() = graphicsIniFile.getBooleanValue("VulkanBrightenAutomap")
        set(value) = graphicsIniFile.setValue("VulkanBrightenAutomap", value)

    var use32bitShading : Boolean
        get() = graphicsIniFile.getBooleanValue("UseVulkan32BitShading")
        set(value) = graphicsIniFile.setValue("UseVulkan32BitShading", value)

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
        get() = gameIniFile.getBooleanValue("InterpolateSectors")
        set(value) = gameIniFile.setValue("InterpolateSectors", value)

    var interpolateMonsters : Boolean
        get() = gameIniFile.getBooleanValue("InterpolateMonsters")
        set(value) = gameIniFile.setValue("InterpolateMonsters", value)

    var interpolateThings : Boolean
        get() = gameIniFile.getBooleanValue("InterpolateMobj")
        set(value) = gameIniFile.setValue("InterpolateMobj", value)

    var interpolateWeapon : Boolean
        get() = gameIniFile.getBooleanValue("InterpolateWeapon")
        set(value) = gameIniFile.setValue("InterpolateWeapon", value)

    var enableLevelTimer : Boolean
        get() = gameIniFile.getBooleanValue("EnableSinglePlayerLevelTimer")
        set(value) = gameIniFile.setValue("EnableSinglePlayerLevelTimer", value)

    var showPerfCounters : Boolean
        get() = gameIniFile.getBooleanValue("ShowPerfCounters")
        set(value) = gameIniFile.setValue("ShowPerfCounters", value)

    var pauseOnFocusLost : Boolean
        get() = gameIniFile.getBooleanValue("PauseOnWindowFocusLost")
        set(value) = gameIniFile.setValue("PauseOnWindowFocusLost",value)

    var fixLineActivation : Boolean
        get() = gameIniFile.getBooleanValue("FixLineActivation")
        set(value) = gameIniFile.setValue("FixLineActivation", value)

    var itemPickupFix : Boolean
        get() = gameIniFile.getBooleanValue("UseItemPickupFix")
        set(value) = gameIniFile.setValue("UseItemPickupFix", value)

    var fixMultiLineCrossing : Boolean
        get() = gameIniFile.getBooleanValue("FixMultiLineSpecialCrossing")
        set(value) = gameIniFile.setValue("FixMultiLineSpecialCrossing", value)

    var fixKillCount : Boolean
        get() = gameIniFile.getBooleanValue("FixKillCount")
        set(value) = gameIniFile.setValue("FixKillCount", value)

    var playerRocketBlastFix : Boolean
        get() = gameIniFile.getBooleanValue("UsePlayerRocketBlastFix")
        set(value) = gameIniFile.setValue("UsePlayerRocketBlastFix", value)

    var fixSpriteVerticalWarp : Boolean
        get() = gameIniFile.getBooleanValue("FixSpriteVerticalWarp")
        set(value) = gameIniFile.setValue("FixSpriteVerticalWarp", value)

    var fixViewBobStrength : Boolean
        get() = gameIniFile.getBooleanValue("FixViewBobStrength")
        set(value) = gameIniFile.setValue("FixViewBobStrength", value)

    var fixGravityStrength : Boolean
        get() = gameIniFile.getBooleanValue("FixGravityStrength")
        set(value) = gameIniFile.setValue("FixGravityStrength", value)

    var useLostSoulSpawnFix : Boolean
        get() = gameIniFile.getBooleanValue("UseLostSoulSpawnFix")
        set(value) = gameIniFile.setValue("UseLostSoulSpawnFix", value)

    var useLineOfSightOverflowFix : Boolean
        get() = gameIniFile.getBooleanValue("UseLineOfSightOverflowFix")
        set(value) = gameIniFile.setValue("UseLineOfSightOverflowFix", value)

    var fixOutdoorBulletPuffs : Boolean
        get() = gameIniFile.getBooleanValue("FixOutdoorBulletPuffs")
        set(value) = gameIniFile.setValue("FixOutdoorBulletPuffs", value)

    var fixBlockingGibsBug : Boolean
        get() = gameIniFile.getBooleanValue("FixBlockingGibsBug")
        set(value) = gameIniFile.setValue("FixBlockingGibsBug", value)

    var useExtendedPlayerShootRange : Boolean
        get() = gameIniFile.getBooleanValue("UseExtendedPlayerShootRange")
        set(value) = gameIniFile.setValue("UseExtendedPlayerShootRange", value)

    var allowMultiMapPickup : Boolean
        get() = gameIniFile.getBooleanValue("AllowMultiMapPickup")
        set(value) = gameIniFile.setValue("AllowMultiMapPickup", value)

    var useMoveInputLatencyTweak : Boolean
        get() = gameIniFile.getBooleanValue("UseMoveInputLatencyTweak")
        set(value) = gameIniFile.setValue("UseMoveInputLatencyTweak", value)

    var useSuperShotgunDelayTweak : Boolean
        get() = gameIniFile.getBooleanValue("UseSuperShotgunDelayTweak")
        set(value) = gameIniFile.setValue("UseSuperShotgunDelayTweak", value)

    var singlePlayerForceSpawnDmThings : Boolean
        get() = gameIniFile.getBooleanValue("SinglePlayerForceSpawnDmThings")
        set(value) = gameIniFile.setValue("SinglePlayerForceSpawnDmThings", value)

    var allowTurningCancellation : Boolean
        get() = gameIniFile.getBooleanValue("AllowTurningCancellation")
        set(value) = gameIniFile.setValue("AllowTurningCancellation", value)

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
        get() = gameIniFile.getBooleanValue("FixSoundPropagation")
        set(value) = gameIniFile.setValue("FixSoundPropagation", value)

    var useDemoTimings : Boolean
        get() = gameIniFile.getBooleanValue("UseDemoTimings")
        set(value) = gameIniFile.setValue("UseDemoTimings", value)

    var enableMapPatchesGamePlay : Boolean
        get() = gameIniFile.getBooleanValue("EnableMapPatches_GamePlay")
        set(value) = gameIniFile.setValue("EnableMapPatches_GamePlay", value)

    var enableMapPatchesVisual : Boolean
        get() = gameIniFile.getBooleanValue("EnableMapPatches_Visual")
        set(value) = gameIniFile.setValue("EnableMapPatches_Visual", value)

    var enableMapPatchesPsyDoom : Boolean
        get() = gameIniFile.getBooleanValue("EnableMapPatches_PsyDoom")
        set(value) = gameIniFile.setValue("EnableMapPatches_PsyDoom", value)

    var enableDevMapAutoReload : Boolean
        get() = cheatsIniFile.getBooleanValue("EnableDevMapAutoReload")
        set(value) = cheatsIniFile.setValue("EnableDevMapAutoReload", value)

    var enableDevCheatShortcuts : Boolean
        get() = cheatsIniFile.getBooleanValue("EnableDevCheatShortcuts")
        set(value) = cheatsIniFile.setValue("EnableDevCheatShortcuts", value)

    var enableDevInPlaceReloadFunctionKey : Boolean
        get() = cheatsIniFile.getBooleanValue("EnableDevInPlaceReloadFunctionKey")
        set(value) = cheatsIniFile.setValue("EnableDevInPlaceReloadFunctionKey", value)

    var coopNoFriendlyFire : Boolean
        get() = multiPlayerIniFile.getBooleanValue("CoopNoFriendlyFire")
        set(value) = multiPlayerIniFile.setValue("CoopNoFriendlyFire", value)

    var coopPreserveKeys : Boolean
        get() = multiPlayerIniFile.getBooleanValue("CoopPreserveKeys")
        set(value) = multiPlayerIniFile.setValue("CoopPreserveKeys", value)

    var coopPreserveWeapons : Boolean
        get() = multiPlayerIniFile.getBooleanValue("CoopPreserveWeapons")
        set(value) = multiPlayerIniFile.setValue("CoopPreserveWeapons", value)

    var coopForceSpawnDeathmatchThings : Boolean
        get() = multiPlayerIniFile.getBooleanValue("CoopForceSpawnDeathmatchThings")
        set(value) = multiPlayerIniFile.setValue("CoopForceSpawnDeathmatchThings", value)

    var dmActivateBossSpecialSectors : Boolean
        get() = multiPlayerIniFile.getBooleanValue("DmActivateBossSpecialSectors")
        set(value) = multiPlayerIniFile.setValue("DmActivateBossSpecialSectors", value)

    var dmExitDisabled : Boolean
        get() = multiPlayerIniFile.getBooleanValue("DmExitDisabled")
        set(value) = multiPlayerIniFile.setValue("DmExitDisabled", value)

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