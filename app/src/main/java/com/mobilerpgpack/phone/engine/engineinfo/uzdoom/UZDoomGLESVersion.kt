package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

enum class UZDoomGLESVersion (val value : Int){
    OpenGLES_2_0 (2),
    OpenGLES_3_0 (3),
    OpenGLES_3_2 (4);

    companion object {
        val stringCollection = entries.map { it.toString() }.toList()
    }
}