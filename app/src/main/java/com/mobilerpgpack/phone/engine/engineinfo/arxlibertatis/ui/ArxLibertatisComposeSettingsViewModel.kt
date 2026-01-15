package com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis.ui

import com.mobilerpgpack.phone.engine.engineinfo.arxlibertatis.ArxLibertatisLocalizationType
import com.mobilerpgpack.phone.engine.engineinfo.utils.viewmodel.IniViewModel
import com.mobilerpgpack.phone.utils.Ini
import java.io.File

class ArxLibertatisComposeSettingsViewModel : IniViewModel() {
    private val cfgIniFile = Ini("${pathToRootUserFolder}${File.separator}ArxLibertatis" +
            "${File.separator}cfg.ini")

    val audioLocalizationAsLiveData = cfgIniFile.getStringValue(AUDIO_LOCALIZATION_KEY,ArxLibertatisLocalizationType.english.name)
    val textLocalizationAsLiveData = cfgIniFile.getStringValue(TEXT_LOCALIZATION_KEY, ArxLibertatisLocalizationType.english.name)
    val fontSizeAsLiveData = cfgIniFile.getFloatValue(FONT_SIZE_KEY)
    val hudScaleAsLiveData = cfgIniFile.getFloatValue(HUD_SCALE_KEY)
    val cursorScaleAsLiveData = cfgIniFile.getFloatValue(CURSOR_SCALE_KEY)

    var audioLocalization : ArxLibertatisLocalizationType get() =
        enumValueOf<ArxLibertatisLocalizationType>(audioLocalizationAsLiveData.value!!)
        set(value) {
            cfgIniFile.setValue(AUDIO_LOCALIZATION_KEY, value.name)
        }

    var textLocalization : ArxLibertatisLocalizationType get() =
        enumValueOf<ArxLibertatisLocalizationType>(textLocalizationAsLiveData.value!!)
        set(value) {
            cfgIniFile.setValue(TEXT_LOCALIZATION_KEY, value.name)
        }

    var fontSize : Float get() = fontSizeAsLiveData.value!!
        set(value) {
            cfgIniFile.setValue(FONT_SIZE_KEY, value)
        }

    var hudScale : Float get() = hudScaleAsLiveData.value!!
        set(value) {
            cfgIniFile.setValue(HUD_SCALE_KEY, value)
        }

    var cursorScale : Float get() = cursorScaleAsLiveData.value!!
        set(value) {
            cfgIniFile.setValue(CURSOR_SCALE_KEY, value)
        }

    override fun reloadIniFiles() {
        super.reloadIniFiles()
        cfgIniFile.load()
    }

    override fun unloadIniFiles() {
        super.unloadIniFiles()
        cfgIniFile.clear()
    }

    private companion object{
        private const val LOCALIZATION_KEY = "language"
        private const val INTERFACE_KEY = "interface"
        private const val AUDIO_LOCALIZATION_KEY = "$LOCALIZATION_KEY.audio"
        private const val TEXT_LOCALIZATION_KEY = "$LOCALIZATION_KEY.string"
        private const val FONT_SIZE_KEY = "$INTERFACE_KEY.font_size"
        private const val HUD_SCALE_KEY = "$INTERFACE_KEY.hud_scale"
        private const val CURSOR_SCALE_KEY = "$INTERFACE_KEY.cursor_scale"
    }
}