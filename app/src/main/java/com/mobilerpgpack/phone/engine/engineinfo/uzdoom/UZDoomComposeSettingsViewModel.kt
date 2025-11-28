package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import com.google.gson.Gson
import com.mobilerpgpack.phone.engine.engineinfo.IniViewModel
import com.mobilerpgpack.phone.utils.Ini
import org.koin.core.component.inject
import java.io.File

class UZDoomComposeSettingsViewModel : IniViewModel(){

    private val uzDoomIni = Ini ("${pathToRootUserFolder}${File.separator}" +
                "uzdoom${File.separator}uzdoom.ini")

    var renderAPI : UZDoomRenderAPI
        get() = UZDoomRenderAPI.fromValue(uzDoomIni.getIntValue(PREFERRED_RENDER_API))
        set(value) = uzDoomIni.setValue(PREFERRED_RENDER_API,value.values.first())

    var autoLoadBrightMaps : Boolean
        get() = uzDoomIni.getBooleanValue(AUTOLOAD_BRIGHTMAPS)
        set(value) = uzDoomIni.setValue(AUTOLOAD_BRIGHTMAPS, value)

    var autoLoadWideScreen: Boolean
        get() = uzDoomIni.getBooleanValue(AUTOLOAD_WIDESCREEN)
        set(value) = uzDoomIni.setValue(AUTOLOAD_WIDESCREEN, value)

    var autoLoadLights : Boolean
        get() = uzDoomIni.getBooleanValue(AUTOLOAD_LIGHTS)
        set(value) = uzDoomIni.setValue(AUTOLOAD_LIGHTS, value)


    override fun reloadIniFiles() {
        uzDoomIni.load()
        super.reloadIniFiles()
    }

    override fun unloadIniFiles() {
        uzDoomIni.clear()
        super.unloadIniFiles()
    }

    private companion object{
        private const val PREFERRED_RENDER_API = "GlobalSettings.vid_preferbackend"
        private const val AUTOLOAD_BRIGHTMAPS = "GlobalSettings.autoloadbrightmaps"
        private const val AUTOLOAD_LIGHTS = "GlobalSettings.autoloadlights"
        private const val AUTOLOAD_WIDESCREEN = "GlobalSettings.autoloadwidescreen"
    }
}

