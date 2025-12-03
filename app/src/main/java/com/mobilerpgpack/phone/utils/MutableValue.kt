package com.mobilerpgpack.phone.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class MutableValue <T> (){

    @Serializable
    private var _value : T? = null

    @Transient
    private var wasInit = false

    @Transient
    private var onValueChanged : ( (newValue : T?) -> Unit)? = null

    private var _mutableValue : T? by mutableStateOf(null)

    var value
        get() = _mutableValue
        set(newValue) {
            _mutableValue = newValue
            _value = newValue
            onValueChanged?.invoke(newValue)
        }

    init {
        _mutableValue = _value
    }

    fun initialize(initialValue : T, onValueChanged : ( (newValue : T?) -> Unit)? = null){
        if (!wasInit){
            if (_value==null){
                _value = initialValue
                _mutableValue = initialValue
            }
            this.onValueChanged = onValueChanged
            wasInit = true
        }
    }
}