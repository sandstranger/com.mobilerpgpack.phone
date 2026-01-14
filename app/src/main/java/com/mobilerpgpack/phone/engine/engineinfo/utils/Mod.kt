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
        pathToMod.liveData.value ?: run { pathToMod.value = "" }
    }

    override fun toString() = "Mod(key='$key', pathToMod=${pathToMod.liveData.value})"

    override fun hashCode() = key.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Mod
        return key == other.key
    }
}