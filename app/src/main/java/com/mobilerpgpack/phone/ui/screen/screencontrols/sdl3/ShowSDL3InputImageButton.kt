package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl3

import android.text.InputType
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState
import com.mobilerpgpack.phone.ui.screen.screencontrols.sdl.ShowSDLInputImageButton
import org.libsdl3.app.onKeyDownTask

class ShowSDL3InputImageButton(
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = ButtonState.Companion.NOT_EXISTING_RES,
    inputType: Int = InputType.TYPE_CLASS_TEXT,
    deleteSymbolsKeyCode: Int = DELETE_SYMBOL_KEYCODE,
    symbolsCountToDeleteBeforeInput : Int = SYMBOLS_COUNT_TO_DELETE_BEFORE_INPUT,
    delayBetweenCharsMs : Long = DEFAULT_DELAY_BETWEEN_CHARS_MS
)  :
    ShowSDLInputImageButton(engineType, offsetXPercent, offsetYPercent, sizePercent,
        alpha, buttonResId, inputType,deleteSymbolsKeyCode, symbolsCountToDeleteBeforeInput, delayBetweenCharsMs) {

    override suspend fun onKeyDown(keyCode: Int, delay: Long, repeatCount: Int) =
        onKeyDownTask(keyCode, delay, delay, repeatCount)
}