package com.mobilerpgpack.phone.utils

import androidx.compose.runtime.mutableStateListOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class ComposeImmutableList <T> () {

    @Serializable
    private val _sourceList = arrayListOf<T>()

    @Serializable
    private val _count = MutableValue<Int>()

    @Transient
    private lateinit var defaultValue: (Int) -> T

    @Transient
    private var onCountValueChanged: (()-> Unit)? = null

    @Transient
    private var wasInit = false

    @Transient
    private var _composeList = mutableStateListOf<T>()

    val sourceList get() = _sourceList

    val composeList get() = _composeList

    var count
        get() = _count.value
        set(value) {
            _count.value = value
            _composeList.clear()
            _composeList+= _sourceList.also {
                if (value ==0){
                    it.clear()
                }
                else{
                    it.resizeTo(value!! ){ index ->
                        defaultValue(index)
                    }
                }
            }
            onCountValueChanged?.invoke()
        }

    init {
        _composeList += _sourceList
        _count.value ?: run { _count.value = 0 }
    }

    fun initialize( defaultValue: (Int) -> T, onCountValueChanged: (()-> Unit)? = null){
        if (!wasInit) {
            this.defaultValue = defaultValue
            this.onCountValueChanged = onCountValueChanged
            wasInit = true
        }
    }

    fun updateComposeList () {
        _composeList.apply {
            clear()
            addAll(_sourceList)
        }
    }
}