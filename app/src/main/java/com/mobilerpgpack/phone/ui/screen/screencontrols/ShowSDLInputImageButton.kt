package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.content.Context
import android.view.KeyEvent
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.utils.PreferencesStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class ShowSDLInputImageButton (
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = NOT_EXISTING_RES,
    private val deleteSymbolsKeyCode: Int = DELETE_SYMBOL_KEYCODE,
    private var symbolsCountToDeleteBeforeInput : Int = SYMBOLS_COUNT_TO_DELETE_BEFORE_INPUT) : KoinComponent,
    ImageButton(SHOW_KEYBOARD_BUTTON_ID, engineType, offsetXPercent, offsetYPercent, sizePercent, alpha, buttonResId) {

    private val preferencesStorage : PreferencesStorage by inject ()

    private val context : Context by inject ()

    override fun onClick() {

    }

    protected abstract fun onKeyDown (keyCode: Int)

    companion object{
        const val SYMBOLS_COUNT_TO_DELETE_BEFORE_INPUT = 1000
        const val DELETE_SYMBOL_KEYCODE = KeyEvent.KEYCODE_DEL
        private const val SHOW_KEYBOARD_BUTTON_ID = "keyboard"

        private fun getKeyCode (charCharacter : Char) : Int{
            val charString = charCharacter.toString()
            return if(keyMap.containsKey(charString)) keyMap[charString]!!.keyCode else KeyEvent.KEYCODE_UNKNOWN
        }

        private val keyMap = mapOf(
            "1" to Key("1", KeyEvent.KEYCODE_1),
            "2" to Key("2", KeyEvent.KEYCODE_2),
            "3" to Key("3", KeyEvent.KEYCODE_3),
            "4" to Key("4", KeyEvent.KEYCODE_4),
            "5" to Key("5", KeyEvent.KEYCODE_5),
            "6" to Key("6", KeyEvent.KEYCODE_6),
            "7" to Key("7", KeyEvent.KEYCODE_7),
            "8" to Key("8", KeyEvent.KEYCODE_8),
            "9" to Key("9", KeyEvent.KEYCODE_9),
            "0" to Key("0", KeyEvent.KEYCODE_0),
            "-" to Key("-", KeyEvent.KEYCODE_MINUS),
            "=" to Key("=", KeyEvent.KEYCODE_EQUALS),
            "Backspace" to Key("Backspace", KeyEvent.KEYCODE_DEL),
            "q" to Key("q", KeyEvent.KEYCODE_Q),
            "w" to Key("w", KeyEvent.KEYCODE_W),
            "e" to Key("e", KeyEvent.KEYCODE_E),
            "r" to Key("r", KeyEvent.KEYCODE_R),
            "t" to Key("t", KeyEvent.KEYCODE_T),
            "y" to Key("y", KeyEvent.KEYCODE_Y),
            "u" to Key("u", KeyEvent.KEYCODE_U),
            "i" to Key("i", KeyEvent.KEYCODE_I),
            "o" to Key("o", KeyEvent.KEYCODE_O),
            "p" to Key("p", KeyEvent.KEYCODE_P),
            "[" to Key("[", KeyEvent.KEYCODE_LEFT_BRACKET),
            "]" to Key("]", KeyEvent.KEYCODE_RIGHT_BRACKET),
            "\\" to Key("\\", KeyEvent.KEYCODE_BACKSLASH),
            "a" to Key("a", KeyEvent.KEYCODE_A),
            "s" to Key("s", KeyEvent.KEYCODE_S),
            "d" to Key("d", KeyEvent.KEYCODE_D),
            "f" to Key("f", KeyEvent.KEYCODE_F),
            "g" to Key("g", KeyEvent.KEYCODE_G),
            "h" to Key("h", KeyEvent.KEYCODE_H),
            "j" to Key("j", KeyEvent.KEYCODE_J),
            "k" to Key("k", KeyEvent.KEYCODE_K),
            "l" to Key("l", KeyEvent.KEYCODE_L),
            ";" to Key(";", KeyEvent.KEYCODE_SEMICOLON),
            "'" to Key("'", KeyEvent.KEYCODE_APOSTROPHE),
            "z" to Key("z", KeyEvent.KEYCODE_Z),
            "x" to Key("x", KeyEvent.KEYCODE_X),
            "c" to Key("c", KeyEvent.KEYCODE_C),
            "v" to Key("v", KeyEvent.KEYCODE_V),
            "b" to Key("b", KeyEvent.KEYCODE_B),
            "n" to Key("n", KeyEvent.KEYCODE_N),
            "m" to Key("m", KeyEvent.KEYCODE_M),
            "," to Key(",", KeyEvent.KEYCODE_COMMA),
            "." to Key(".", KeyEvent.KEYCODE_PERIOD),
            "/" to Key("/", KeyEvent.KEYCODE_SLASH),
            "@" to Key("@", KeyEvent.KEYCODE_AT),
            " " to Key(" ", KeyEvent.KEYCODE_SPACE),
            "\\n" to Key("\\n", KeyEvent.KEYCODE_ENTER),
            "\\t" to Key("\\t", KeyEvent.KEYCODE_TAB),
            ":" to Key(":", KeyEvent.KEYCODE_SEMICOLON),
            "L-CTRL" to Key("L-CTRL", KeyEvent.KEYCODE_CTRL_LEFT),
            "SPACE" to Key("SPACE", KeyEvent.KEYCODE_SPACE),
            "LEFT" to Key("LEFT", KeyEvent.KEYCODE_DPAD_LEFT),
            "DOWN" to Key("DOWN", KeyEvent.KEYCODE_DPAD_DOWN),
            "RIGHT" to Key("RIGHT", KeyEvent.KEYCODE_DPAD_RIGHT)
        )

        private data class Key(val label: String, val keyCode: Int)
    }
}