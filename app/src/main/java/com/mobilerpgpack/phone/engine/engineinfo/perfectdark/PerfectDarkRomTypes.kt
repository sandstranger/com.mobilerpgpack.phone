package com.mobilerpgpack.phone.engine.engineinfo.perfectdark

import com.mobilerpgpack.phone.main.PERFECT_DARK_JPN_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.PERFECT_DARK_NTSC_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.PERFECT_DARK_PAL_NATIVE_LIB_NAME

enum class PerfectDarkRomTypes (val mainLibraryName : String) {
    NTSC (PERFECT_DARK_NTSC_NATIVE_LIB_NAME),
    PAL (PERFECT_DARK_PAL_NATIVE_LIB_NAME),
    JPN (PERFECT_DARK_JPN_NATIVE_LIB_NAME);

    companion object{
        val DefaultRomType = NTSC
    }
}