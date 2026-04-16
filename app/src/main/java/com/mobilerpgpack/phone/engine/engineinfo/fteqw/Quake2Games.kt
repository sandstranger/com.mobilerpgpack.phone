package com.mobilerpgpack.phone.engine.engineinfo.fteqw

import com.mobilerpgpack.phone.main.QUAKE2_BOT_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.QUAKE2_CTF_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.QUAKE2_ZAERO_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.ROGUE_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.XATRIX_NATIVE_LIB_NAME
import com.mobilerpgpack.phone.main.YQUAKE2_CORE_NATIVE_LIB_NAME

enum class Quake2Games(val description: String,
                       val directoryName : String,
                       val nativeLibraryName : String) {
    Quake2("Quake2: (baseq2/)","baseq2",
        YQUAKE2_CORE_NATIVE_LIB_NAME),
    Quake2_CTF("Quake2: Capture the flag (ctf/)","ctf",
        QUAKE2_CTF_NATIVE_LIB_NAME),
    Quake2_Rogue("Quake2: Missionpack Ground zero (rogue/)","rogue",
        ROGUE_NATIVE_LIB_NAME),
    Quake2_Xatrix("Quake2: Missionpack The Reckoning (xatrix/)","xatrix",
        XATRIX_NATIVE_LIB_NAME),
    Quake2_Zaero("Quake2: Missionpack Zaero (zaero/)","zaero",
        QUAKE2_ZAERO_NATIVE_LIB_NAME),
    Quake2_3zb2("Quake2: The 3rd Zigock Bot II mod (3zb2/)","3zb2",
        QUAKE2_BOT_NATIVE_LIB_NAME);

    companion object {
        val DefaultGame = Quake2
        val descriptions = Quake2Games.entries.map { it.description }.toList()
        fun fromValue(description: String) = Quake2Games.entries.find { it.description == description }!!
    }
}