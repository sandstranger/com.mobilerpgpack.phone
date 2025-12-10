package com.mobilerpgpack.phone.ui.screen.screencontrols.sdl

import android.annotation.SuppressLint
import android.content.Context
import android.view.KeyEvent
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.engine.engineinfo.IEngineInfo
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.ui.screen.screencontrols.ImageButton
import com.mobilerpgpack.phone.utils.IKeyCodesProvider
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.quantuminventions.customkeyboard.components.expandableView.ExpandableState
import com.quantuminventions.customkeyboard.components.expandableView.ExpandableStateListener
import com.quantuminventions.customkeyboard.components.keyboard.CustomKeyboardView
import com.quantuminventions.customkeyboard.components.keyboard.KeyboardListener
import com.quantuminventions.customkeyboard.components.keyboard.controllers.KeyboardController
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named

abstract class ShowSDLInputImageButton(
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = NOT_EXISTING_RES,
    private val keyboardInputType : CustomKeyboardView.KeyboardType = DEFAULT_KEYBOARD_INPUT_TYPE) : KoinComponent, KeyboardListener, ExpandableStateListener,
    ImageButton(SHOW_KEYBOARD_BUTTON_ID, engineType, offsetXPercent, offsetYPercent, sizePercent, alpha,
        buttonResId)
{
    private var wasInit = false

    private val keyCodesProvider : IKeyCodesProvider by inject ()

    private val engineInfo by lazy {
        runBlocking {
            val preferencesStorage : PreferencesStorage = get ()
            get <IEngineInfo> (named(preferencesStorage.activeEngineAsFlowString.first()))
        }
    }

    @SuppressLint("CheckResult")
    override fun onClick(context: Context) {
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

    final override fun characterClicked(c: Char) = onKeyDown(keyCodesProvider.getKeyCode(c))

    final override fun specialKeyClicked(key: KeyboardController.SpecialKey) {
        when (key) {
            KeyboardController.SpecialKey.DELETE,
            KeyboardController.SpecialKey.BACKSPACE -> onKeyDown(DELETE_SYMBOL_KEYCODE)
            KeyboardController.SpecialKey.BACK -> onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT)
            KeyboardController.SpecialKey.NEXT -> onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT)
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

    private fun setupKeyboard(){
        if (wasInit){
            return
        }
        wasInit = true
        engineInfo.keyboardView?.apply {
            setKeyCodeListener (this@ShowSDLInputImageButton)
            registerListener(this@ShowSDLInputImageButton)
            registerEditText(keyboardInputType, engineInfo.keyboardInputField!!)
        }
    }

    companion object {
        private const val DELETE_SYMBOL_KEYCODE = KeyEvent.KEYCODE_DEL

        private const val SHOW_KEYBOARD_BUTTON_ID = "keyboard"

        val DEFAULT_KEYBOARD_INPUT_TYPE = CustomKeyboardView.KeyboardType.QWERTY
    }
}