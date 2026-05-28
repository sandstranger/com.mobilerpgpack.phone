package com.mobilerpgpack.phone.engine

enum class GlesRenderVersions {
    OpenGLES_2_0,
    OpenGLES_3_X;

    companion object{
        val DefaultValue = OpenGLES_2_0
    }
}