package com.mobilerpgpack.phone.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    private var _composeList by mutableStateOf(emptyList<T>())

    val sourceList get() = _sourceList

    val composeList get() = _composeList

    var count
        get() = _count.value
        set(value) {
            _count.value = value
            _composeList = _sourceList.let {
                if (value ==0){
                    it.clear()
                }
                else{
                    it.resizeTo(value!! ){ index ->
                        defaultValue(index)
                    }
                }
                it.toList()
            }
            onCountValueChanged?.invoke()
        }

    init {
        _composeList = _sourceList.toList()
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
        _composeList = _sourceList.toList()
    }
}