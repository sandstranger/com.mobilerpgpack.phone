package com.mobilerpgpack.phone.main

import com.mobilerpgpack.phone.BuildConfig

const val TRANSLATOR_NATIVE_LIB_NAME = "Translator"

const val DOOM64_MAIN_ENGINE_LIB = "DOOM64"

const val DOOMRPG_MAIN_ENGINE_LIB = "DoomRPG"

const val DOOM2RPG_MAIN_ENGINE_LIB = "DoomIIRPG"

const val WOLFENSTEINRPG_MAIN_ENGINE_LIB = "WolfensteinRPG"

const val SDL3_NATIVE_LIB_NAME = "SDL3"

const val FMOD_NATIVE_LIB_NAME = "fmod"

const val SDL2_NATIVE_LIB_NAME = "SDL2"

const val OPENAL_NATIVE_LIB_NAME = "openal"

const val SDL2_TTF_NATIVE_LIB_NAME = "SDL2_ttf"

const val FLUIDSYNTH_NATIVE_LIB_NAME = "fluidsynth"

const val GME_NATIVE_LIB_NAME = "gme"

const val OBOE_NATIVE_LUB_NAME = "oboe"

const val SDL2_MIXER_NATIVE_LIB_NAME = "SDL2_mixer"

const val SPIRV_NATIVE_LIB_NAME = "spirv-cross-c-shared"

val PNG_NATIVE_LIB_NAME = if (BuildConfig.DEBUG) "png18d" else "png18"

val gl4esLibraryName = if (BuildConfig.LEGACY_GLES2) "gl4es" else "ng_gl4es"

val gl4esFullLibraryName = buildFullLibraryName(gl4esLibraryName)

fun buildFullLibraryName (libraryName : String ) = "lib${libraryName}.so"
