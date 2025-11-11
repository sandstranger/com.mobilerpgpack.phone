package com.mobilerpgpack.phone.ui.screen.screencontrols

import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES

class ShowSDL3InputImageButton(
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = NOT_EXISTING_RES,
    deleteSymbolsKeyCode: Int = DELETE_SYMBOL_KEYCODE,
    symbolsCountToDeleteBeforeInput : Int = SYMBOLS_COUNT_TO_DELETE_BEFORE_INPUT,
    delayBetweenCharsMs : Long = DEFAULT_DELAY_BETWEEN_CHARS_MS)  :
    ShowSDLInputImageButton(engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha, buttonResId, deleteSymbolsKeyCode, symbolsCountToDeleteBeforeInput, delayBetweenCharsMs) {

    override suspend fun onKeyDown(keyCode: Int, delay: Long, repeatCount: Int) =
        org.libsdl3.app.onKeyDownTask(keyCode, delay, delay, repeatCount)
}