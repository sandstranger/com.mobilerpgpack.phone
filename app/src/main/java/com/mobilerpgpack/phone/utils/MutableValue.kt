package com.mobilerpgpack.phone.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    @Transient
    private val _liveData = MutableLiveData <T?>(null)

    val liveData get() : LiveData<T?> = _liveData

    var value
        get() = _value
        set(newValue) {
            _liveData.value = newValue
            _value = newValue
            onValueChanged?.invoke(newValue)
        }

    init {
        _liveData.value = _value
    }

    fun initialize(initialValue : T, onValueChanged : ( (newValue : T?) -> Unit)? = null){
        if (!wasInit){
            if (_value==null){
                _value = initialValue
                _liveData.value = initialValue
            }
            this.onValueChanged = onValueChanged
            wasInit = true
        }
    }
}