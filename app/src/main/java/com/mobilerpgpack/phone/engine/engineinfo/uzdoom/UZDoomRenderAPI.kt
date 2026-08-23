package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

enum class UZDoomRenderAPI (val value : Int){
    OpenGLES (2),
    OpenGL (0);

    companion object {
        fun fromValue(value: Int): UZDoomRenderAPI {
            return entries.find { it.value == value }!!
        }
    }
}