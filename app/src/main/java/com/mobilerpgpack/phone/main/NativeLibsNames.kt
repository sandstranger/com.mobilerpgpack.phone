package com.mobilerpgpack.phone.main

import com.mobilerpgpack.phone.BuildConfig

const val TRANSLATOR_NATIVE_LIB_NAME = "Translator"

const val PSYDOOM_MAIN_ENGINE_LIB = "PsyDoom"

const val UZDOOM_MAIN_ENGINE_LIB = "uzdoom"

const val DOOM64_MAIN_ENGINE_LIB = "DOOM64"

const val DOOM64_ENHANCED_MAIN_ENGINE_LIB = "DOOM64-Enhanced"

const val DOOMRPG_MAIN_ENGINE_LIB = "DoomRPG"

const val DOOM2RPG_MAIN_ENGINE_LIB = "DoomIIRPG"

const val WOLFENSTEINRPG_MAIN_ENGINE_LIB = "WolfensteinRPG"

const val SDL3_NATIVE_LIB_NAME = "SDL3"

const val FMOD_NATIVE_LIB_NAME = "fmod"

const val SDL3HELPER_NATIVE_LIB_NAME = "SDL3Helper"

const val PERFECT_DARK_JPN_NATIVE_LIB_NAME = "pd-jpn-final"

const val PERFECT_DARK_NTSC_NATIVE_LIB_NAME = "pd-ntsc-final"

const val PERFECT_DARK_PAL_NATIVE_LIB_NAME = "pd-pal-final"

const val SDL2_NATIVE_LIB_NAME = "SDL2"

const val C_PLUS_PLUS_SHARED_NATIVE_LIB_NAME = "c++_shared"

const val ZMUSIC_NATIVE_LIB_NAME = "zmusic"

const val OPENAL_NATIVE_LIB_NAME = "openal"

const val SDL2_TTF_NATIVE_LIB_NAME = "SDL2_ttf"

const val FLUIDSYNTH_NATIVE_LIB_NAME = "fluidsynth"

const val GME_NATIVE_LIB_NAME = "gme"

const val OBOE_NATIVE_LUB_NAME = "oboe"

const val SDL2_MIXER_NATIVE_LIB_NAME = "SDL2_mixer"

const val MPG123_NATIVE_LIB_NAME = "mpg123"

const val OGG_NATIVE_LIB_NAME = "ogg"

const val MP3LAME_NATIVE_LIB_NAME = "mp3lame"

const val VORBIS_NATIVE_LIB_NAME = "vorbis"

const val OPUS_NATIVE_LIB_NAME = "opus"

const val FLAC_NATIVE_LIB_NAME = "FLAC"

const val SND_FILE_NATIVE_LIB_NAME = "sndfile"

val FREETYPE_NATIVE_LIB_NAME = if (BuildConfig.DEBUG) "freetyped" else "freetype"

val PNG_NATIVE_LIB_NAME = if (BuildConfig.DEBUG) "png16d" else "png16"

val gl4esLibraryName = if (BuildConfig.LEGACY_GLES2) "gl4es" else "ng_gl4es"

val gl4esFullLibraryName = buildFullLibraryName(gl4esLibraryName)

fun buildFullLibraryName (libraryName : String ) = "lib${libraryName}.so"
