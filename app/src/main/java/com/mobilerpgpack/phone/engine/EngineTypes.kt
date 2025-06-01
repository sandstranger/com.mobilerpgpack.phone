package com.mobilerpgpack.phone.engine



enum class EngineTypes {
    WolfensteingRpg,
    DoomRpg,
    Doom2Rpg;

    companion object{
        val DefaultActiveEngine = WolfensteingRpg
    }
}