package com.mobilerpgpack.phone.engine



enum class EngineTypes {
    WolfensteinRpg,
    DoomRpg,
    Doom2Rpg;

    companion object{
        val DefaultActiveEngine = WolfensteinRpg
    }
}