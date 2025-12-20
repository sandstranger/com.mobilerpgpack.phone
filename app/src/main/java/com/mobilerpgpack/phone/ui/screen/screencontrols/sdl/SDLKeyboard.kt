package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import com.mobilerpgpack.phone.utils.IKeyCodesProvider
import com.quantuminventions.customkeyboard.components.expandableView.ExpandableState
import com.quantuminventions.customkeyboard.components.expandableView.ExpandableStateListener
import com.quantuminventions.customkeyboard.components.keyboard.CustomKeyboardView
import com.quantuminventions.customkeyboard.components.keyboard.CustomisedKeyboardView
import com.quantuminventions.customkeyboard.components.keyboard.KeyboardListener
import com.quantuminventions.customkeyboard.components.keyboard.controllers.KeyboardController
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class SDLKeyboard : KoinComponent, KeyboardListener, ExpandableStateListener {
    private var keyboardInputType : CustomKeyboardView.KeyboardType = DEFAULT_KEYBOARD_INPUT_TYPE
    private var useReturnButton = false
    private var wasInit = false
    private var keyboardInputField : TextView? = null
    private var keyboardView : CustomisedKeyboardView? = null
    private val keyCodesProvider : IKeyCodesProvider by inject ()

    fun initialize(keyboardInputField : EditText, keyboardView : CustomisedKeyboardView){
        if (this.keyboardInputField==null || this.keyboardView==null){
            this.keyboardInputField = keyboardInputField
            this.keyboardView = keyboardView
        }
    }

    fun showKeyboard(useReturnButton : Boolean = false,
                     keyboardInputType : CustomKeyboardView.KeyboardType = DEFAULT_KEYBOARD_INPUT_TYPE){
        this.keyboardInputType = keyboardInputType
        this.useReturnButton = useReturnButton
        if (keyboardInputField == null || keyboardView == null){
            return
        }
        setupKeyboard()
        keyboardInputField?.apply {
            text = ""
            requestFocus()
            clearFocus()
        }
    }

    final override fun characterClicked(c: Char) {
        onKeyDown(keyCodesProvider.getKeyCode(c))
        onCharClicked(c)
    }

    final override fun specialKeyClicked(key: KeyboardController.SpecialKey) {
        when (key) {
            KeyboardController.SpecialKey.DELETE,
            KeyboardController.SpecialKey.BACKSPACE -> onKeyDown(DELETE_SYMBOL_KEYCODE)
            KeyboardController.SpecialKey.BACK -> onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT)
            KeyboardController.SpecialKey.NEXT -> onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT)
            KeyboardController.SpecialKey.DONE -> {
                if (useReturnButton) {
                    onKeyDown(KeyEvent.KEYCODE_ENTER)
                }
            }
            else -> {}
        }
    }

    final override fun onStateChange(state: ExpandableState) {
        when (state) {
            ExpandableState.COLLAPSED,
            ExpandableState.COLLAPSING -> {
                keyboardInputField!!.text = ""
                keyboardView!!.clearFocus()
            }
            else -> {}
        }
    }

    protected abstract fun onKeyDown(keyCode: Int)

    protected abstract fun onCharClicked (char: Char)

    private fun setupKeyboard(){
        if (wasInit){
            return
        }
        wasInit = true
        keyboardView?.apply {
            setKeyCodeListener (this@SDLKeyboard)
            registerListener(this@SDLKeyboard)
            registerEditText(keyboardInputType, keyboardInputField!!)
        }
    }

    companion object {
        private const val DELETE_SYMBOL_KEYCODE = KeyEvent.KEYCODE_DEL
        val DEFAULT_KEYBOARD_INPUT_TYPE = CustomKeyboardView.KeyboardType.QWERTY
    }
}