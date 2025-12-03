package com.mobilerpgpack.phone.engine.engineinfo.utils

import com.mobilerpgpack.phone.utils.MutableValue
import kotlinx.serialization.Serializable

@Serializable
data class Mod (var index : Int){

    val pathToMod = MutableValue<String>()

    init {
        pathToMod.value ?:run { pathToMod.value = "" }
    }
}