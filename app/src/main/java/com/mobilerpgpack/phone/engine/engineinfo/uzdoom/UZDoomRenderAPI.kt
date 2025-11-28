package com.mobilerpgpack.phone.engine.engineinfo.uzdoom

enum class UZDoomRenderAPI (vararg args: Int){
    OpenGLES (2,0),
    Vulkan (1);

    val values: IntArray = args

    companion object {
        val stringCollection = entries.map { it.toString() }.toList()

        fun fromValue(value: Int): UZDoomRenderAPI {
            return entries.find { it.values.contains(value) }!!
        }
    }
}