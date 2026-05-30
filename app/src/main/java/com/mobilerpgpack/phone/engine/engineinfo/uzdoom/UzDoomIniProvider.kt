package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import com.mobilerpgpack.phone.engine.engineinfo.uzdoom.UZDoomComposeSettingsViewModel.Companion.PREFERRED_RENDER_API
import com.mobilerpgpack.phone.utils.Ini
import java.io.File

class UzDoomIniProvider {
    private val uzDoomIni = Ini ("uzdoom${File.separator}uzdoom.ini", removeSpacesBetweenSeparator = true)
    private val renderAPIAsLiveData by lazy { uzDoomIni.getIntValue(PREFERRED_RENDER_API) }
    val useOpenGLESRender get() = UZDoomRenderAPI.fromValue(renderAPIAsLiveData.value!!) == UZDoomRenderAPI.OpenGLES
}