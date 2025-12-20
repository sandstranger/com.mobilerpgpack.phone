package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import android.annotation.SuppressLint
import android.content.Context
import android.view.KeyEvent
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.utils.IKeyCodesProvider
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.getBlockingValue
import com.quantuminventions.customkeyboard.components.expandableView.ExpandableState
import com.quantuminventions.customkeyboard.components.expandableView.ExpandableStateListener
import com.quantuminventions.customkeyboard.components.keyboard.CustomKeyboardView
import com.quantuminventions.customkeyboard.components.keyboard.KeyboardListener
import com.quantuminventions.customkeyboard.components.keyboard.controllers.KeyboardController
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.getValue

abstract class SDLKeyboard : KoinComponent, KeyboardListener, ExpandableStateListener {
    private var keyboardInputType : CustomKeyboardView.KeyboardType = DEFAULT_KEYBOARD_INPUT_TYPE
    private var useReturnButton = false
    private var wasInit = false
    private val keyCodesProvider : IKeyCodesProvider by inject ()

    private val engineInfo by lazy {
        val preferencesStorage : PreferencesStorage = get ()
        get <IEngineInfo> (named(preferencesStorage.activeEngineAsFlowString.getBlockingValue()))
    }

    fun showKeyboard(useReturnButton : Boolean = false,
                     keyboardInputType : CustomKeyboardView.KeyboardType = DEFAULT_KEYBOARD_INPUT_TYPE){
        this.keyboardInputType = keyboardInputType
        this.useReturnButton = useReturnButton
        if (engineInfo.keyboardInputField == null || engineInfo.keyboardView == null){
            return
        }
        setupKeyboard()
        engineInfo.keyboardInputField?.apply {
            text = ""
            requestFocus()
            clearFocus()
        }
        engineInfo.rootView!!.requestFocus()
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
                engineInfo.keyboardInputField!!.text = ""
                engineInfo.keyboardView!!.clearFocus()
                engineInfo.rootView!!.requestFocus()
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
        engineInfo.keyboardView?.apply {
            setKeyCodeListener (this@SDLKeyboard)
            registerListener(this@SDLKeyboard)
            registerEditText(keyboardInputType, engineInfo.keyboardInputField!!)
        }
    }

    companion object {
        private const val DELETE_SYMBOL_KEYCODE = KeyEvent.KEYCODE_DEL
        val DEFAULT_KEYBOARD_INPUT_TYPE = CustomKeyboardView.KeyboardType.QWERTY
    }
}