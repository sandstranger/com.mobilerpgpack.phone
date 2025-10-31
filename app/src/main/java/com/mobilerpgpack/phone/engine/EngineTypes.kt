package com.mobilerpgpack.phone.engine

enum class EngineTypes {
    WolfensteinRpg,
    DoomRpg,
    Doom2Rpg,
    Doom64ExPlus;

    companion object{
        val DefaultActiveEngine = WolfensteinRpg
    }
}