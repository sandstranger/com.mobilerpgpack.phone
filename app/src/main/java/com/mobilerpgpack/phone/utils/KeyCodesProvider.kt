package com.mobilerpgpack.phone.utils

import android.view.KeyCharacterMap
import android.view.KeyEvent

class KeyCodesProvider : IKeyCodesProvider {

    private val charMap = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD)

    private val charArray : CharArray = CharArray(1)

    private val cachedKeyEvents = mutableMapOf<Char, Int>()

    override fun getKeyCode (charItem : Char) : Int{
        if (cachedKeyEvents.containsKey(charItem)){
            return cachedKeyEvents[charItem]!!
        }

        charArray[0] = charItem
        val events : Array<KeyEvent>? = charMap.getEvents(charArray)
        return if (!events.isNullOrEmpty()) {
            val keyCode = events[0].keyCode
            cachedKeyEvents[charItem] = keyCode
            return keyCode
        } else KeyEvent.KEYCODE_UNKNOWN
    }
}