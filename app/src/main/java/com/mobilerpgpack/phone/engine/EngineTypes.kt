package com.mobilerpgpack.phone.engine

enum class EngineTypes {
    WolfensteinRpg,
    DoomRpg,
    Doom2Rpg,
    Doom64ExPlus,
    Doom64ExPlusEnhanced,
    PsyDoom,
    UZDoom,
    PerfectDark,
    ArxLibertatis,
    FTEQW,
    Widelands,
    VanillaConquer,
    Dhewm3;

    companion object{
        val DefaultActiveEngine = WolfensteinRpg
    }
}