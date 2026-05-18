package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import com.mobilerpgpack.phone.engine.EngineTypes
import com.sun.jna.Native
import org.libsdl3.app.SDLSurface

class DoomRpgEngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>) :
    DoomRPGSeriesEngineInfo(mainEngineLib, allLibs,  EngineTypes.DoomRpg) {

    override val needToShowScreenControls: Boolean = true

    override val pathToResource get() = super.preferencesStorage.pathToDoomRpgZipFile.value!!

    override fun isMouseShown() = false

    override val requiredResourceExtensions = listOf(".zip", ".ZIP")

    override val loadGL4ES = false

    override val allowedToEnableAngle = false

    private external fun setScreenResolution (screenWidth : Int, screenHeight : Int)

    private external fun setRecalculateScreenResolutionsState (recalculateScreenResolutions : Boolean)

    override fun onNativeLibrariesLoaded() {
        super.onNativeLibrariesLoaded()
        Native.register(DoomRpgEngineInfo::class.java, mainLibraryName)
        recalculateGameScreenResolution()
    }

    private fun recalculateGameScreenResolution() {
        val (width, height) = getDefaultDoomRpgResolution()

        val savedDoomRpgScreenWidth = preferencesStorage.getIntValue(preferencesStorage.savedDoomRpgScreenWidthPrefsKey).value!!
        val savedDoomRpgScreenHeight= preferencesStorage.getIntValue(preferencesStorage.savedDoomRpgScreenHeightPrefsKey).value!!

        if (savedDoomRpgScreenWidth != width && savedDoomRpgScreenHeight != height) {
            preferencesStorage.setIntValue(preferencesStorage.savedDoomRpgScreenWidthPrefsKey, width)
            preferencesStorage.setIntValue(preferencesStorage.savedDoomRpgScreenHeightPrefsKey, height)
            setRecalculateScreenResolutionsState(true)
        } else {
            setRecalculateScreenResolutionsState(false)
        }
        setScreenResolution(width, height)
    }

    private fun getDefaultDoomRpgResolution(): Pair<Int, Int> {
        if (SDLSurface.fixedWidth > 0 && SDLSurface.fixedHeight > 0) {
            return SDLSurface.fixedWidth to SDLSurface.fixedHeight
        }

        return resolution.screenWidth to resolution.screenHeight
    }
}

