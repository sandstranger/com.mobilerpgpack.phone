package com.mobilerpgpack.phone.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.collections.List

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
    private val _composeList = MutableLiveData(emptyList<T>())

    val sourceList get() = _sourceList

    val composeList get() : LiveData<List<T>> = _composeList

    val countAsLiveData get() : LiveData<Int?> = _count.liveData

    var count
        get() = _count.value!!
        set(value) {
            _count.value = value
            _composeList.value = _sourceList.let {
                if (value == 0) {
                    it.clear()
                } else {
                    it.resizeTo(value) { index ->
                        defaultValue(index)
                    }
                }
                it.toList()
            }
            onCountValueChanged?.invoke()
        }

    init {
        _composeList.value = _sourceList.toList()
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
        _composeList.value = _sourceList.toList()
    }
}