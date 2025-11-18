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
        get() = gameIniFile.getIntValue("PauseOnWindowFocusLost") == 1
        set(value) = gameIniFile.setIntValue("PauseOnWindowFocusLost", if (value) 1 else 2)

    var bobScale : Float
        get() = gameIniFile.getFloatValue("ViewBobbingStrength").coerceIn(0.0f,1.0f)
        set(value) = gameIniFile.setFloatValue("ViewBobbingStrength", value.coerceIn(0.0f,1.0f))

    var heapSize : Int
        get() = gameIniFile.getIntValue("MainMemoryHeapSize")
        set(value) = gameIniFile.setIntValue("MainMemoryHeapSize", value)

    private fun unloadIniFiles(){
        _iniFilesLoaded = false
        graphicsIniFile.unload()
    }

    private fun reloadIniFiles (){
        graphicsIniFile.reload()
        _iniFilesLoaded = true
    }

    private class Ini (pathToFile : String ){

        private val iniFile = File (pathToFile)

        private var iniConfig : INIConfiguration? = null

        private val loaded get() = iniFile.exists() && iniConfig!=null

        fun getBooleanValue (key: String) = getIntValue(key) > 0

        fun setBooleanValue (key: String, value: Boolean) = setIntValue(key, if (value) 1 else 0)

        fun getFloatValue (key: String) : Float {
            if (!loaded){
                reload()
            }
            return if (loaded) iniConfig!!.getFloat(key) else 0.0f
        }

        fun setFloatValue (key: String, value : Float) = setValue(key, value)

        fun getIntValue (key: String) : Int{
            if (!loaded){
                reload()
            }
            return if (loaded) iniConfig!!.getInt(key) else 0
        }

        fun setIntValue (key: String, value : Int) = setValue(key, value)

        private fun <T> setValue (key: String, value: T){
            if (!loaded){
                reload()
            }

            if (loaded) {
                FileWriter(iniFile.absolutePath).use {
                    iniConfig!!.setProperty(key, value)
                    iniConfig!!.write(it)
                }
            }
        }

        fun unload(){
            iniConfig = null
        }

        fun reload (){
            unload()
            if (iniFile.exists()) {
                iniConfig = configs.fileBased(INIConfiguration::class.java, iniFile.absolutePath)
            }
        }

        private companion object{
            private val configs : Configurations = Configurations()
        }
    }
}