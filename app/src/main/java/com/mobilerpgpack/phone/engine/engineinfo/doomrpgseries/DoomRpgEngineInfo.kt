package com.mobilerpgpack.phone.engine.engineinfo.doomrpgseries

import android.system.Os
import androidx.activity.ComponentActivity
import com.mobilerpgpack.phone.engine.EngineTypes
import org.libsdl.app.SDLSurface

class DoomRpgEngineInfo(
    mainEngineLib: String,
    allLibs: Array<String>) :
    DoomRPGSeriesEngineInfo(mainEngineLib, allLibs,  EngineTypes.DoomRpg) {

    private var savedDoomRpgScreenWidth: Int = 0
    private var savedDoomRpgScreenHeight: Int = 0

    override val needToShowScreenControls: Boolean = true

    override val pathToResource get() = super.preferencesStorage.pathToDoomRpgZipFile

    override fun isMouseShown() = false

    override val requiredResourceExtensions = listOf(".zip", ".ZIP")

    override fun initialize(activity: ComponentActivity) {
        super.initialize(activity)
        recalculateGameScreenResolution()
    }

    private fun recalculateGameScreenResolution() {
        val (width, height) = getDefaultDoomRpgResolution()

        savedDoomRpgScreenWidth = preferencesStorage.getIntValue(preferencesStorage.savedDoomRpgScreenWidthPrefsKey)
        savedDoomRpgScreenHeight= preferencesStorage.getIntValue(preferencesStorage.savedDoomRpgScreenHeightPrefsKey)

        if (savedDoomRpgScreenWidth != width && savedDoomRpgScreenHeight != height) {
            preferencesStorage.setIntValue(preferencesStorage.savedDoomRpgScreenWidthPrefsKey, width)
            preferencesStorage.setIntValue(preferencesStorage.savedDoomRpgScreenHeightPrefsKey, height)

            Os.setenv("RECALCULATE_RESOLUTION_INDEX", "true", true)
        } else {
            Os.setenv("RECALCULATE_RESOLUTION_INDEX", "false", true)
        }
        Os.setenv("SCREEN_WIDTH", width.toString(), true)
        Os.setenv("SCREEN_HEIGHT", height.toString(), true)
        Os.setenv("FORCE_FILE_PATH", "true", true)
    }

    private fun getDefaultDoomRpgResolution(): Pair<Int, Int> {
        if (SDLSurface.fixedWidth > 0 && SDLSurface.fixedHeight > 0) {
            return SDLSurface.fixedWidth to SDLSurface.fixedHeight
        }

        return resolution.screenWidth to resolution.screenHeight
    }
}

