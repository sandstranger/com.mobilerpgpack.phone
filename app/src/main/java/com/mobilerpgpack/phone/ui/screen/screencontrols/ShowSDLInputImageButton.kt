package com.mobilerpgpack.phone.ui.screen.screencontrols

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.view.KeyEvent
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.mobilerpgpack.phone.R
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState.Companion.NOT_EXISTING_RES
import com.mobilerpgpack.phone.utils.IKeyCodesProvider
import com.mobilerpgpack.phone.utils.PreferencesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class ShowSDLInputImageButton(
    engineType: EngineTypes,
    offsetXPercent: Float = 0f,
    offsetYPercent: Float = 0f,
    sizePercent: Float = 0.13f,
    alpha: Float = 0.65f,
    buttonResId: Int = NOT_EXISTING_RES,
    private val inputType: Int = InputType.TYPE_CLASS_TEXT,
    private val deleteSymbolsKeyCode: Int = DELETE_SYMBOL_KEYCODE,
    private var symbolsCountToDeleteBeforeInput: Int = SYMBOLS_COUNT_TO_DELETE_BEFORE_INPUT,
    private val delayBetweenCharsMs : Long = DEFAULT_DELAY_BETWEEN_CHARS_MS) : KoinComponent,
    ImageButton(
        SHOW_KEYBOARD_BUTTON_ID,
        engineType,
        offsetXPercent,
        offsetYPercent,
        sizePercent,
        alpha,
        buttonResId
    ) {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val preferencesStorage: PreferencesStorage by inject()

    private val keycodesProvider : IKeyCodesProvider by inject ()

    override var show: Boolean = true

    override val enabled: Boolean
        get() {
            return preferencesStorage.let {
                runBlocking {
                    !it.useStandardSDLTextInput.first()
                }
            }
        }

    @SuppressLint("CheckResult")
    override fun onClick(context: Context) {
        MaterialDialog(context).show {
            input (inputType = inputType)
            { _, text -> scope.launch { enterText(text) } }
            positiveButton(R.string.ok_text)
            negativeButton(R.string.cancel_text)
            title(R.string.sdl_virtual_input)
        }
    }

    protected abstract suspend fun onKeyDown(keyCode: Int, delay : Long, repeatCount: Int = 1)

    private suspend fun enterText(text: CharSequence) {
        onKeyDown(deleteSymbolsKeyCode, delayBetweenCharsMs, symbolsCountToDeleteBeforeInput)

        if (symbolsCountToDeleteBeforeInput < text.length) {
            symbolsCountToDeleteBeforeInput = text.length
        }

        text.forEach {
            onKeyDown(keycodesProvider.getKeyCode(it), delayBetweenCharsMs)
        }
    }

    companion object {
        const val DEFAULT_DELAY_BETWEEN_CHARS_MS : Long = 2L
        const val SYMBOLS_COUNT_TO_DELETE_BEFORE_INPUT = 100
        const val DELETE_SYMBOL_KEYCODE = KeyEvent.KEYCODE_DEL

        private const val SHOW_KEYBOARD_BUTTON_ID = "keyboard"
    }
}