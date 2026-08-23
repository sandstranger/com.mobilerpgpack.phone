package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import android.content.Context
import com.jakewharton.processphoenix.ProcessPhoenix
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.utils.UZDoomModsModel
import com.mobilerpgpack.phone.engine.engineinfo.utils.viewmodel.IniViewModel
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.utils.Ini
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class UZDoomComposeSettingsViewModel : IniViewModel(){
    private val prefsStorage : UZDoomPreferenceStorage by inject()
    private val context : Context by inject ()
    private val scope : CoroutineScope by inject (
        named(KoinModulesProvider.MAIN_THREAD_COROUTINE_KEY))
    private val uzDoomIni : Ini by lazy {
        Ini ("${if (prefsStorage.uzDoomEngineVersion.value == UZDoomEngineVersions.Dev)
            UZDoomEngineInfo.DEV_GZDOOM_USER_FOLDER_NAME else UZDoomEngineInfo.LEGACY_GZDOOM_USER_FOLDER_NAME}/uzdoom.ini", removeSpacesBetweenSeparator = true)
    }
    @Volatile
    private var isEngineVersionSaving = false

    val uzDoomMods : UZDoomModsModel by inject (named(EngineTypes.UZDoom.toString()))

    val renderAPIAsLiveData by lazy {
        uzDoomIni.getIntValue(PREFERRED_RENDER_API)
    }
    val autoLoadBrightMapsAsLiveData by lazy {
        uzDoomIni.getBooleanValue(AUTOLOAD_BRIGHTMAPS)
    }
    val autoLoadWideScreenAsLiveData by lazy {
        uzDoomIni.getBooleanValue(AUTOLOAD_WIDESCREEN)
    }
    val autoLoadLightsAsLiveData by lazy {
        uzDoomIni.getBooleanValue(AUTOLOAD_LIGHTS)
    }

    val useOpenGLESRender get() = renderAPI == UZDoomRenderAPI.OpenGLES

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

    fun onEngineVersionChanged(engineVersion: UZDoomEngineVersions){
        if (prefsStorage.uzDoomEngineVersion.value!! != engineVersion && !isEngineVersionSaving){
            isEngineVersionSaving = true
            scope.launch {
                prefsStorage.setEnumValueAsync(prefsStorage.uzDoomEngineVersionPrefsKey, engineVersion)
                isEngineVersionSaving = false
                ProcessPhoenix.triggerRebirth(context)
            }
        }
    }

    companion object{
        private const val AUTOLOAD_BRIGHTMAPS = "GlobalSettings.autoloadbrightmaps"
        private const val AUTOLOAD_LIGHTS = "GlobalSettings.autoloadlights"
        private const val AUTOLOAD_WIDESCREEN = "GlobalSettings.autoloadwidescreen"
        const val PREFERRED_RENDER_API = "GlobalSettings.vid_preferbackend"
    }
}

