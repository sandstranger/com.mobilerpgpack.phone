package com.mobilerpgpack.phone.engine.engineinfo.utils

import com.mobilerpgpack.phone.utils.MutableValue
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class Mod {

    private var _key: String? = null

    val pathToMod = MutableValue<String>()

    val key get() = _key!!

    init {
        _key ?: run { _key = UUID.randomUUID().toString() }
        pathToMod.value ?: run { pathToMod.value = "" }
    }
}