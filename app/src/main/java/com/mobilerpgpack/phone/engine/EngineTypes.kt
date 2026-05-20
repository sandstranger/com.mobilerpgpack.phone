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
    Classic_RBDOOM_3_BFG;

    companion object{
        val DefaultActiveEngine = UZDoom
    }
}