package com.mobilerpgpack.phone.engine.engineinfo.fteqw

// attackWithLeftMouseButton: the on-screen attack button sends the left mouse button instead of Enter
enum class FTEQWGames (val commandLineArg : String, val attackWithLeftMouseButton : Boolean = false) {
    Quake ("-quake"),
    Quake2 ("-quake2"),
    Quake3("-quake3"),
    Hexen2 ("-hexen2"),
    CustomGame("", attackWithLeftMouseButton = true),
    HalfLife ("-halflife", attackWithLeftMouseButton = true)
}