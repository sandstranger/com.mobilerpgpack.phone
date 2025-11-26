package com.mobilerpgpack.phone.engine

enum class EngineTypes {
    WolfensteinRpg,
    DoomRpg,
    Doom2Rpg,
    Doom64ExPlus,
    Doom64ExPlusEnhanced,
    PsyDoom,
    UZDoom;

    companion object{
        val DefaultActiveEngine = WolfensteinRpg
    }
}