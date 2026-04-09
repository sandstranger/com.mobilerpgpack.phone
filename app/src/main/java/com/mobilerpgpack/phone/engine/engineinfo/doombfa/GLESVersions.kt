package com.mobilerpgpack.phone.engine.engineinfo.doombfa

enum class GLESVersions (val glesIntVersion : Int) {
    OpenGL_ES_3_0 (300),
    OpenGL_ES_3_1 (310),
    OpenGL_ES_3_2 (320);

    companion object{
        val defaultGLESVersion = OpenGL_ES_3_2
    }
}