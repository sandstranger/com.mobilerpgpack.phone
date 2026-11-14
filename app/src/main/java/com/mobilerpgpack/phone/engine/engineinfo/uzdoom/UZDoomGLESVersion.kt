package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

// The GLES renderer targets OpenGL ES 2.0 or 3.0. "OpenGLES_3_2" (value 4) existed before; it is
// still accepted from old preferences and mapped to OpenGLES_3_0.
enum class UZDoomGLESVersion (val value : Int){
    OpenGLES_2_0 (2),
    OpenGLES_3_0 (3);

    companion object {
        val stringCollection = entries.map { it.toString() }.toList()

        fun fromStoredName(name: String?): UZDoomGLESVersion =
            entries.firstOrNull { it.name == name } ?: OpenGLES_3_0
    }
}
