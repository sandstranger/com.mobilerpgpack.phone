package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

import com.google.gson.Gson
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class UZDoomModsModel : KoinComponent {

    private val gson : Gson by inject ()

    private var _enableModsSupport = false

    private var _modsCount = 0

    private var _pathToMods = emptyArray<String?>()

    var enableModsSupport
        get() = _enableModsSupport
        set(value) {_enableModsSupport = value}

    var modsCount
        get() = _modsCount
        set(value) {
            if (_modsCount!=value && value >= 0){
                _modsCount = value
                _pathToMods = if (value == 0) emptyArray() else arrayOfNulls(value)
            }
        }
}