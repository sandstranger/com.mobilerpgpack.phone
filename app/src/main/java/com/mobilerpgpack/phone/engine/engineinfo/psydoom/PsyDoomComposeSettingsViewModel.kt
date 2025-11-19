package com.mobilerpgpack.phone.engine.engineinfo.psydoom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.IAssetExtractor
import org.apache.commons.configuration2.INIConfiguration
import org.apache.commons.configuration2.builder.fluent.Configurations
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class PsyDoomComposeSettingsViewModel : ViewModel(), KoinComponent {

    private var _iniFilesLoaded by mutableStateOf(false)

    private val assetsExtractor : IAssetExtractor = get ()

    private val pathToRootUserFolder: String = get(
        named(KoinModulesProvider.USER_ROOT_FOLDER_NAMED_KEY))

    private val pathToPsyDoomConfigsFolder = "${pathToRootUserFolder}${File.separator}" +
            "com.codelobster${File.separator}PsyDoom"

    private val graphicsIniFile = Ini("${pathToPsyDoomConfigsFolder}${File.separator}graphics_cfg.ini")

    private val gameIniFile = Ini("${pathToPsyDoomConfigsFolder}${File.separator}game_cfg.ini")

    private val inputIniFile = Ini("${pathToPsyDoomConfigsFolder}${File.separator}input_cfg.ini")

    private val audioIniFile = Ini("${pathToPsyDoomConfigsFolder}${File.separator}audio_cfg.ini")

    private val cheatsIniFile = Ini("${pathToPsyDoomConfigsFolder}${File.separator}cheats_cfg.ini")

    private val multiPlayerIniFile = Ini("${pathToPsyDoomConfigsFolder}${File.separator}multiplayer_cfg.ini")

    val iniFilesLoaded get() = _iniFilesLoaded

    init {
        assetsExtractor.assetsStartedCopyListeners += { unloadIniFiles() }
        assetsExtractor.assetsFinishCopyListeners += { reloadIniFiles() }

        if (assetsExtractor.assetsCopied){
            reloadIniFiles()
        }
    }

    var enableVsync : Boolean
        get() = graphicsIniFile.getBooleanValue("EnableVSync")
        set(value) = graphicsIniFile.setBooleanValue("EnableVSync", value)

    var useExtendedAutomapColors : Boolean
        get() = graphicsIniFile.getBooleanValue("UseExtendedAutomapColors")
        set(value) = graphicsIniFile.setBooleanValue("UseExtendedAutomapColors", value)

    var vulkanPixelsStretch : Boolean
        get() = graphicsIniFile.getBooleanValue("VulkanPixelStretch")
        set(value) = graphicsIniFile.setBooleanValue("VulkanPixelStretch", value)

    var widescreenEnabled : Boolean
        get() = graphicsIniFile.getBooleanValue("VulkanWidescreenEnabled")
        set(value) = graphicsIniFile.setBooleanValue("VulkanWidescreenEnabled", value)

    var drawExtendedStatusBar : Boolean
        get() = graphicsIniFile.getBooleanValue("VulkanDrawExtendedStatusBar")
        set(value) = graphicsIniFile.setBooleanValue("VulkanDrawExtendedStatusBar", value)

    var disableVulkanRender : Boolean
        get() = graphicsIniFile.getBooleanValue("DisableVulkanRenderer")
        set(value) = graphicsIniFile.setBooleanValue("DisableVulkanRenderer", value)

    var enhanceWallDrawPrecision : Boolean
        get() = graphicsIniFile.getBooleanValue("EnhanceWallDrawPrecision")
        set(value) = graphicsIniFile.setBooleanValue("EnhanceWallDrawPrecision", value)

    var skyLeakFix : Boolean
        get() = graphicsIniFile.getBooleanValue("SkyLeakFix")
        set(value) = graphicsIniFile.setBooleanValue("SkyLeakFix", value)

    var floorGapRenderFix : Boolean
        get() = graphicsIniFile.getBooleanValue("FloorRenderGapFix")
        set(value) = graphicsIniFile.setBooleanValue("FloorRenderGapFix", value)

    var tripleBuffer : Boolean
        get() = graphicsIniFile.getBooleanValue("VulkanTripleBuffer")
        set(value) = graphicsIniFile.setBooleanValue("VulkanTripleBuffer", value)

    var brightenAutomap : Boolean
        get() = graphicsIniFile.getBooleanValue("VulkanBrightenAutomap")
        set(value) = graphicsIniFile.setBooleanValue("VulkanBrightenAutomap", value)

    var use32bitShading : Boolean
        get() = graphicsIniFile.getBooleanValue("UseVulkan32BitShading")
        set(value) = graphicsIniFile.setBooleanValue("UseVulkan32BitShading", value)

    var topOverscanPixels : Int
        get() = graphicsIniFile.getIntValue("TopOverscanPixels")
        set(value) = graphicsIniFile.setIntValue("TopOverscanPixels", value)

    var renderHeight : Int
        get() = graphicsIniFile.getIntValue("VulkanRenderHeight")
        set(value) = graphicsIniFile.setIntValue("VulkanRenderHeight", value)

    var antialiasingMultisamples : Int
        get() = graphicsIniFile.getIntValue("AntiAliasingMultisamples")
        set(value) = graphicsIniFile.setIntValue("AntiAliasingMultisamples", value)

    var vramSizeInMbytes : Int
        get() = graphicsIniFile.getIntValue("VramSizeInMegabytes")
        set(value) = graphicsIniFile.setIntValue("VramSizeInMegabytes", value)

    var bottomOverscanPixels : Int
        get() = graphicsIniFile.getIntValue("BottomOverscanPixels")
        set(value) = graphicsIniFile.setIntValue("BottomOverscanPixels", value)

    var logicalDisplayWidth : Int
        get() = graphicsIniFile.getIntValue("LogicalDisplayWidth")
        set(value) = graphicsIniFile.setIntValue("LogicalDisplayWidth", value)

    var interpolateSectors : Boolean
        get() = gameIniFile.getBooleanValue("InterpolateSectors")
        set(value) = gameIniFile.setBooleanValue("InterpolateSectors", value)

    var interpolateMonsters : Boolean
        get() = gameIniFile.getBooleanValue("InterpolateMonsters")
        set(value) = gameIniFile.setBooleanValue("InterpolateMonsters", value)

    var interpolateThings : Boolean
        get() = gameIniFile.getBooleanValue("InterpolateMobj")
        set(value) = gameIniFile.setBooleanValue("InterpolateMobj", value)

    var interpolateWeapon : Boolean
        get() = gameIniFile.getBooleanValue("InterpolateWeapon")
        set(value) = gameIniFile.setBooleanValue("InterpolateWeapon", value)

    var enableLevelTimer : Boolean
        get() = gameIniFile.getBooleanValue("EnableSinglePlayerLevelTimer")
        set(value) = gameIniFile.setBooleanValue("EnableSinglePlayerLevelTimer", value)

    var showPerfCounters : Boolean
        get() = gameIniFile.getBooleanValue("ShowPerfCounters")
        set(value) = gameIniFile.setBooleanValue("ShowPerfCounters", value)

    var pauseOnFocusLost : Boolean
        get() = gameIniFile.getBooleanValue("PauseOnWindowFocusLost")
        set(value) = gameIniFile.setBooleanValue("PauseOnWindowFocusLost",value)

    var fixLineActivation : Boolean
        get() = gameIniFile.getBooleanValue("FixLineActivation")
        set(value) = gameIniFile.setBooleanValue("FixLineActivation", value)

    var itemPickupFix : Boolean
        get() = gameIniFile.getBooleanValue("UseItemPickupFix")
        set(value) = gameIniFile.setBooleanValue("UseItemPickupFix", value)

    var fixMultiLineCrossing : Boolean
        get() = gameIniFile.getBooleanValue("FixMultiLineSpecialCrossing")
        set(value) = gameIniFile.setBooleanValue("FixMultiLineSpecialCrossing", value)

    var fixKillCount : Boolean
        get() = gameIniFile.getBooleanValue("FixKillCount")
        set(value) = gameIniFile.setBooleanValue("FixKillCount", value)

    var playerRocketBlastFix : Boolean
        get() = gameIniFile.getBooleanValue("UsePlayerRocketBlastFix")
        set(value) = gameIniFile.setBooleanValue("UsePlayerRocketBlastFix", value)

    var fixSpriteVerticalWarp : Boolean
        get() = gameIniFile.getBooleanValue("FixSpriteVerticalWarp")
        set(value) = gameIniFile.setBooleanValue("FixSpriteVerticalWarp", value)

    var fixViewBobStrength : Boolean
        get() = gameIniFile.getBooleanValue("FixViewBobStrength")
        set(value) = gameIniFile.setBooleanValue("FixViewBobStrength", value)

    var fixGravityStrength : Boolean
        get() = gameIniFile.getBooleanValue("FixGravityStrength")
        set(value) = gameIniFile.setBooleanValue("FixGravityStrength", value)

    var useLostSoulSpawnFix : Boolean
        get() = gameIniFile.getBooleanValue("UseLostSoulSpawnFix")
        set(value) = gameIniFile.setBooleanValue("UseLostSoulSpawnFix", value)

    var useLineOfSightOverflowFix : Boolean
        get() = gameIniFile.getBooleanValue("UseLineOfSightOverflowFix")
        set(value) = gameIniFile.setBooleanValue("UseLineOfSightOverflowFix", value)

    var fixOutdoorBulletPuffs : Boolean
        get() = gameIniFile.getBooleanValue("FixOutdoorBulletPuffs")
        set(value) = gameIniFile.setBooleanValue("FixOutdoorBulletPuffs", value)

    var fixBlockingGibsBug : Boolean
        get() = gameIniFile.getBooleanValue("FixBlockingGibsBug")
        set(value) = gameIniFile.setBooleanValue("FixBlockingGibsBug", value)

    var useExtendedPlayerShootRange : Boolean
        get() = gameIniFile.getBooleanValue("UseExtendedPlayerShootRange")
        set(value) = gameIniFile.setBooleanValue("UseExtendedPlayerShootRange", value)

    var allowMultiMapPickup : Boolean
        get() = gameIniFile.getBooleanValue("AllowMultiMapPickup")
        set(value) = gameIniFile.setBooleanValue("AllowMultiMapPickup", value)

    var useMoveInputLatencyTweak : Boolean
        get() = gameIniFile.getBooleanValue("UseMoveInputLatencyTweak")
        set(value) = gameIniFile.setBooleanValue("UseMoveInputLatencyTweak", value)

    var useSuperShotgunDelayTweak : Boolean
        get() = gameIniFile.getBooleanValue("UseSuperShotgunDelayTweak")
        set(value) = gameIniFile.setBooleanValue("UseSuperShotgunDelayTweak", value)

    var singlePlayerForceSpawnDmThings : Boolean
        get() = gameIniFile.getBooleanValue("SinglePlayerForceSpawnDmThings")
        set(value) = gameIniFile.setBooleanValue("SinglePlayerForceSpawnDmThings", value)

    var allowTurningCancellation : Boolean
        get() = gameIniFile.getBooleanValue("AllowTurningCancellation")
        set(value) = gameIniFile.setBooleanValue("AllowTurningCancellation", value)

    var allowMovementCancellation : PsyDoomComposeSettings.GameEnum
        get() = PsyDoomComposeSettings.GameEnum.fromValue(gameIniFile.getIntValue("AllowMovementCancellation"))!!
        set(value) = gameIniFile.setIntValue("AllowMovementCancellation", value.value)

    var useFinalDoomPlayerMovement : PsyDoomComposeSettings.GameEnum
        get() = PsyDoomComposeSettings.GameEnum.fromValue(gameIniFile.getIntValue("UseFinalDoomPlayerMovement"))!!
        set(value) = gameIniFile.setIntValue("UseFinalDoomPlayerMovement", value.value)

    var usePalTimings : PsyDoomComposeSettings.TickMode
        get() = PsyDoomComposeSettings.TickMode.fromValue(gameIniFile.getIntValue("UsePalTimings"))!!
        set(value) = gameIniFile.setIntValue("UsePalTimings", value.value)

    var fixSoundPropagation : Boolean
        get() = gameIniFile.getBooleanValue("FixSoundPropagation")
        set(value) = gameIniFile.setBooleanValue("FixSoundPropagation", value)

    var useDemoTimings : Boolean
        get() = gameIniFile.getBooleanValue("UseDemoTimings")
        set(value) = gameIniFile.setBooleanValue("UseDemoTimings", value)

    var enableMapPatchesGamePlay : Boolean
        get() = gameIniFile.getBooleanValue("EnableMapPatches_GamePlay")
        set(value) = gameIniFile.setBooleanValue("EnableMapPatches_GamePlay", value)

    var enableMapPatchesVisual : Boolean
        get() = gameIniFile.getBooleanValue("EnableMapPatches_Visual")
        set(value) = gameIniFile.setBooleanValue("EnableMapPatches_Visual", value)

    var enableMapPatchesPsyDoom : Boolean
        get() = gameIniFile.getBooleanValue("EnableMapPatches_PsyDoom")
        set(value) = gameIniFile.setBooleanValue("EnableMapPatches_PsyDoom", value)

    var useFastLoading : Boolean
        get() = gameIniFile.getBooleanValue("UseFastLoading")
        set(value) = gameIniFile.setBooleanValue("UseFastLoading", value)

    var skipIntros : Boolean
        get() = gameIniFile.getBooleanValue("SkipIntros")
        set(value) = gameIniFile.setBooleanValue("SkipIntros", value)

    var enableDevMapAutoReload : Boolean
        get() = cheatsIniFile.getBooleanValue("EnableDevMapAutoReload")
        set(value) = cheatsIniFile.setBooleanValue("EnableDevMapAutoReload", value)

    var enableDevCheatShortcuts : Boolean
        get() = cheatsIniFile.getBooleanValue("EnableDevCheatShortcuts")
        set(value) = cheatsIniFile.setBooleanValue("EnableDevCheatShortcuts", value)

    var enableDevInPlaceReloadFunctionKey : Boolean
        get() = cheatsIniFile.getBooleanValue("EnableDevInPlaceReloadFunctionKey")
        set(value) = cheatsIniFile.setBooleanValue("EnableDevInPlaceReloadFunctionKey", value)

    var coopNoFriendlyFire : Boolean
        get() = multiPlayerIniFile.getBooleanValue("CoopNoFriendlyFire")
        set(value) = multiPlayerIniFile.setBooleanValue("CoopNoFriendlyFire", value)

    var coopPreserveKeys : Boolean
        get() = multiPlayerIniFile.getBooleanValue("CoopPreserveKeys")
        set(value) = multiPlayerIniFile.setBooleanValue("CoopPreserveKeys", value)

    var coopPreserveWeapons : Boolean
        get() = multiPlayerIniFile.getBooleanValue("CoopPreserveWeapons")
        set(value) = multiPlayerIniFile.setBooleanValue("CoopPreserveWeapons", value)

    var coopForceSpawnDeathmatchThings : Boolean
        get() = multiPlayerIniFile.getBooleanValue("CoopForceSpawnDeathmatchThings")
        set(value) = multiPlayerIniFile.setBooleanValue("CoopForceSpawnDeathmatchThings", value)

    var dmActivateBossSpecialSectors : Boolean
        get() = multiPlayerIniFile.getBooleanValue("DmActivateBossSpecialSectors")
        set(value) = multiPlayerIniFile.setBooleanValue("DmActivateBossSpecialSectors", value)

    var dmExitDisabled : Boolean
        get() = multiPlayerIniFile.getBooleanValue("DmExitDisabled")
        set(value) = multiPlayerIniFile.setBooleanValue("DmExitDisabled", value)

    var coopPreserveAmmoFactor : PsyDoomComposeSettings.RespawnAmmoEnum
        get() = PsyDoomComposeSettings.RespawnAmmoEnum.fromValue(multiPlayerIniFile.getIntValue("CoopPreserveAmmoFactor"))!!
        set(value) = multiPlayerIniFile.setIntValue("CoopPreserveAmmoFactor", value.value)

    var dmFragLimit : Int
        get() = multiPlayerIniFile.getIntValue("DmFragLimit").coerceIn(0, Int.MAX_VALUE)
        set(value) = multiPlayerIniFile.setIntValue("DmFragLimit", value.coerceIn(0, Int.MAX_VALUE))

    var cheatKeySequenceGodMode : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_GodMode")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()){
                cheatsIniFile.setStringValue("CheatKeySequence_GodMode", value)
            }
        }

    var cheatKeySequenceNoClip : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_NoClip")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setStringValue("CheatKeySequence_NoClip", value)
            }
        }

    var cheatKeySequenceLevelWarp : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_LevelWarp")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setStringValue("CheatKeySequence_LevelWarp", value)
            }
        }

    var cheatKeySequenceWeaponsKeysAndArmor : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_WeaponsKeysAndArmor")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setStringValue("CheatKeySequence_WeaponsKeysAndArmor", value)
            }
        }

    var cheatKeySequenceWeaponsAndArmor : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_WeaponsAndArmor")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()){
                cheatsIniFile.setStringValue("CheatKeySequence_WeaponsAndArmor", value)
            }
        }

    var cheatKeySequenceAllMapLinesOn : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_AllMapLinesOn")
        set(value)  {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setStringValue("CheatKeySequence_AllMapLinesOn", value)
            }
        }

    var cheatKeySequenceAllMapThingsOn : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_AllMapThingsOn")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setStringValue("CheatKeySequence_AllMapThingsOn", value)
            }
        }

    var cheatKeySequenceXRayVision : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_XRayVision")
        set(value)  {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setStringValue("CheatKeySequence_XRayVision", value)
            }
        }

    var cheatKeySequenceVramViewer : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_VramViewer")
        set(value)  {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setStringValue("CheatKeySequence_VramViewer", value)
            }
        }

    var cheatKeySequenceNoTarget : String
        get() = cheatsIniFile.getStringValue("CheatKeySequence_NoTarget")
        set(value) {
            if (value.isNotEmpty() && value.isNotBlank()) {
                cheatsIniFile.setStringValue("CheatKeySequence_NoTarget", value)
            }
        }

    var bobScale : Float
        get() = gameIniFile.getFloatValue("ViewBobbingStrength").coerceIn(0.0f, Float.MAX_VALUE)
        set(value) = gameIniFile.setFloatValue("ViewBobbingStrength", value.coerceIn(0.0f, Float.MAX_VALUE))

    var heapSize : Int
        get() = gameIniFile.getIntValue("MainMemoryHeapSize")
        set(value) = gameIniFile.setIntValue("MainMemoryHeapSize", value)

    var lostSoulSpawnLimit : Int
        get() = gameIniFile.getIntValue("LostSoulSpawnLimit")
        set(value) = gameIniFile.setIntValue("LostSoulSpawnLimit", value)

    var mouseTurnSpeed : Int
        get() = inputIniFile.getIntValue("MouseTurnSpeed").coerceIn(0, Int.MAX_VALUE)
        set(value) = inputIniFile.setIntValue("MouseTurnSpeed", value.coerceIn(0, Int.MAX_VALUE))

    var gamepadFastTurnSpeedHigh : Int
        get() = inputIniFile.getIntValue("GamepadFastTurnSpeed_High").coerceIn(0, Int.MAX_VALUE)
        set(value) = inputIniFile.setIntValue("GamepadFastTurnSpeed_High", value.coerceIn(0, Int.MAX_VALUE))

    var gamepadFastTurnSpeedLow : Int
        get() = inputIniFile.getIntValue("GamepadFastTurnSpeed_Low").coerceIn(0, Int.MAX_VALUE)
        set(value) = inputIniFile.setIntValue("GamepadFastTurnSpeed_Low", value.coerceIn(0, Int.MAX_VALUE))

    var gamepadTurnSpeedHigh : Int
        get() = inputIniFile.getIntValue("GamepadTurnSpeed_High").coerceIn(0, Int.MAX_VALUE)
        set(value) = inputIniFile.setIntValue("GamepadTurnSpeed_High", value.coerceIn(0, Int.MAX_VALUE))

    var gamepadTurnSpeedLow : Int
        get() = inputIniFile.getIntValue("GamepadTurnSpeed_Low").coerceIn(0, Int.MAX_VALUE)
        set(value) = inputIniFile.setIntValue("GamepadTurnSpeed_Low", value.coerceIn(0, Int.MAX_VALUE))

    var audioBufferSize : Int
        get() = audioIniFile.getIntValue("AudioBufferSize").coerceIn(0, Int.MAX_VALUE)
        set(value) = audioIniFile.setIntValue("AudioBufferSize", value.coerceIn(0, Int.MAX_VALUE))

    var spuRamSize : Int
        get() = audioIniFile.getIntValue("SpuRamSize")
        set(value) = audioIniFile.setIntValue("SpuRamSize",value)

    var gamepadDeadZone : Float
        get() = inputIniFile.getFloatValue("GamepadDeadZone").coerceIn(0.0f, 1.0f)
        set(value) = inputIniFile.setFloatValue("GamepadDeadZone", value.coerceIn(0.0f, 1.0f))

    var analogToDigitalThreshold : Float
        get() = inputIniFile.getFloatValue("AnalogToDigitalThreshold").coerceIn(0.0f, 1.0f)
        set(value) = inputIniFile.setFloatValue("AnalogToDigitalThreshold", value.coerceIn(0.0f, 1.0f))

    private fun unloadIniFiles(){
        _iniFilesLoaded = false
        graphicsIniFile.unload()
        gameIniFile.unload()
        inputIniFile.unload()
        audioIniFile.unload()
        cheatsIniFile.unload()
        multiPlayerIniFile.unload()
    }

    private fun reloadIniFiles (){
        graphicsIniFile.reload()
        gameIniFile.reload()
        inputIniFile.reload()
        audioIniFile.reload()
        cheatsIniFile.reload()
        multiPlayerIniFile.reload()
        _iniFilesLoaded = true
    }

    private class Ini (pathToFile : String ){

        private val iniFile = File (pathToFile)

        private var iniConfig = INIConfiguration()

        private var _loaded = false

        private val loaded get() = iniFile.exists() && _loaded

        fun getBooleanValue (key: String) = getIntValue(key) > 0

        fun setBooleanValue (key: String, value: Boolean) = setIntValue(key, if (value) 1 else 0)

        fun getFloatValue (key: String) : Float {
            if (!loaded){
                reload()
            }
            return if (loaded && iniConfig.containsKey(key)) iniConfig.getFloat(key) else 0.0f
        }

        fun setFloatValue (key: String, value : Float) = setValue(key, value)

        fun getStringValue (key: String) : String {
            if (!loaded){
                reload()
            }
            return if (loaded && iniConfig.containsKey(key)) iniConfig.getString(key) else ""
        }

        fun setStringValue (key: String, value : String) = setValue(key, value)

        fun getIntValue (key: String) : Int{
            if (!loaded){
                reload()
            }
            return if (loaded && iniConfig.containsKey(key)) iniConfig.getInt(key) else 0
        }

        fun setIntValue (key: String, value : Int) = setValue(key, value)

        private fun <T> setValue (key: String, value: T){
            if (!loaded){
                reload()
            }

            if (loaded) {
                FileWriter(iniFile.absolutePath).use {
                    iniConfig.setProperty(key, value)
                    iniConfig.write(it)
                }
            }
        }

        fun unload(){
            _loaded = false
            iniConfig.clear()
        }

        fun reload (){
            unload()
            if (iniFile.exists()) {
                FileReader(iniFile.absolutePath).use {
                    iniConfig.read(it)
                }
                _loaded = true
            }
        }
    }
}