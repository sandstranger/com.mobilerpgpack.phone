package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.utils.UZDoomModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.viewmodel.IniViewModel
import com.mobilerpgpack.phone.utils.Ini
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.File

class UZDoomComposeSettingsViewModel : IniViewModel(){
    private val uzDoomIni = Ini ("uzdoom${File.separator}uzdoom.ini", removeSpacesBetweenSeparator = true)
    private val uzDoomInstance : UZDoomEngineInfo by inject ()

    val uzDoomMods : UZDoomModsModel = get (named(EngineTypes.UZDoom.toString()))
    val renderAPIAsLiveData = uzDoomIni.getIntValue(PREFERRED_RENDER_API)
    val autoLoadBrightMapsAsLiveData = uzDoomIni.getBooleanValue(AUTOLOAD_BRIGHTMAPS)
    val autoLoadWideScreenAsLiveData = uzDoomIni.getBooleanValue(AUTOLOAD_WIDESCREEN)
    val autoLoadLightsAsLiveData = uzDoomIni.getBooleanValue(AUTOLOAD_LIGHTS)

    var renderAPI : UZDoomRenderAPI
        get() = UZDoomRenderAPI.fromValue(renderAPIAsLiveData.value!!)
        set(value) = uzDoomIni.setValue(PREFERRED_RENDER_API,value.value)

    var autoLoadBrightMaps : Boolean
        get() = autoLoadBrightMapsAsLiveData.value!!
        set(value) = uzDoomIni.setValue(AUTOLOAD_BRIGHTMAPS, value)

    var autoLoadWideScreen: Boolean
        get() = autoLoadWideScreenAsLiveData.value!!
        set(value) = uzDoomIni.setValue(AUTOLOAD_WIDESCREEN, value)

    var autoLoadLights : Boolean
        get() = autoLoadLightsAsLiveData.value!!
        set(value) = uzDoomIni.setValue(AUTOLOAD_LIGHTS, value)

    override fun reloadIniFiles() {
        uzDoomIni.load()
        super.reloadIniFiles()
    }

    override fun unloadIniFiles() {
        uzDoomIni.clear()
        super.unloadIniFiles()
    }

    companion object{
        private const val AUTOLOAD_BRIGHTMAPS = "GlobalSettings.autoloadbrightmaps"
        private const val AUTOLOAD_LIGHTS = "GlobalSettings.autoloadlights"
        private const val AUTOLOAD_WIDESCREEN = "GlobalSettings.autoloadwidescreen"
        const val PREFERRED_RENDER_API = "GlobalSettings.vid_preferbackend"
    }
}

