package com.mobilerpgpack.phone.main

import android.content.Context
import com.mobilerpgpack.phone.BuildConfig

const val TRANSLATOR_NATIVE_LIB_NAME = "Translator"

const val PSYDOOM_MAIN_ENGINE_LIB = "PsyDoom"

const val UZDOOM_MAIN_ENGINE_LIB = "uzdoom"

const val DOOM64_MAIN_ENGINE_LIB = "DOOM64"

const val DOOM64_ENHANCED_MAIN_ENGINE_LIB = "DOOM64-Enhanced"

const val DOOMRPG_MAIN_ENGINE_LIB = "DoomRPG"

const val DOOM2RPG_MAIN_ENGINE_LIB = "DoomIIRPG"

const val WOLFENSTEINRPG_MAIN_ENGINE_LIB = "WolfensteinRPG"

const val ARX_LIBERTATIS_MAIN_ENGINE_LIB  = "arx"

const val DOOM_BFA_MAIN_LIB = "DoomBFA"

const val FTEQW_MAIN_ENGINE_LIB = "ftedroid"

const val WIDELANDS_MAIN_ENGINE_LIB = "widelands"

const val YQUAKE2_CORE_NATIVE_LIB_NAME = "yquake2"

const val XATRIX_NATIVE_LIB_NAME = "xatrix"

const val ROGUE_NATIVE_LIB_NAME = "rogue"

const val QUAKE2_CTF_NATIVE_LIB_NAME = "ctf"

const val QUAKE2_BOT_NATIVE_LIB_NAME = "3zb2"

const val QUAKE2_ZAERO_NATIVE_LIB_NAME = "zaero"

const val TIBERIAN_DAWN_NATIVE_LIB_NAME = "vanillatd"

const val RED_ALERT_NATIVE_LIB_NAME = "vanillara"

const val BZ2_NATIVE_LIB_NAME = "bz2"

const val ODE_NATIVE_LIB_NAME = "ode"

const val JPEG_NATIVE_LIB_NAME = "jpeg"

const val SDL3_NATIVE_LIB_NAME = "SDL3"

const val GLOB_NATIVE_LIB_NAME = "glob"

const val FMOD_NATIVE_LIB_NAME = "fmod"

const val TIFF_NATIVE_LIB_NAME = "tiff"

const val TIFFXX_NATIVE_LIB_NAME = "tiffxx"

const val LZO_NATIVE_LIB_NAME = "lzo2"

const val SDL3HELPER_NATIVE_LIB_NAME = "SDL3Helper"

const val PERFECT_DARK_JPN_NATIVE_LIB_NAME = "pd-jpn-final"

const val PERFECT_DARK_NTSC_NATIVE_LIB_NAME = "pd-ntsc-final"

const val PERFECT_DARK_PAL_NATIVE_LIB_NAME = "pd-pal-final"

const val SDL2_NATIVE_LIB_NAME = "SDL2"

const val C_PLUS_PLUS_SHARED_NATIVE_LIB_NAME = "c++_shared"

const val ZMUSIC_NATIVE_LIB_NAME = "zmusic"

const val OPENAL_NATIVE_LIB_NAME = "openal"

const val SDL2_TTF_NATIVE_LIB_NAME = "SDL2_ttf"

const val SDL2_IMAGE_NATIVE_LIB_NAME = "SDL2_image"

const val FLUIDSYNTH_NATIVE_LIB_NAME = "fluidsynth"

const val GME_NATIVE_LIB_NAME = "gme"

const val OBOE_NATIVE_LUB_NAME = "oboe"

const val SDL2_MIXER_NATIVE_LIB_NAME = "SDL2_mixer"

const val MPG123_NATIVE_LIB_NAME = "mpg123"

const val OGG_NATIVE_LIB_NAME = "ogg"

const val MP3LAME_NATIVE_LIB_NAME = "mp3lame"

const val VORBIS_NATIVE_LIB_NAME = "vorbis"

const val VORBIS_FILE_NATIVE_LIB_NAME = "vorbisfile"

const val VORBIS_ENC_NATIVE_LIB_NAME = "vorbisenc"

const val OPUS_NATIVE_LIB_NAME = "opus"

const val FLAC_NATIVE_LIB_NAME = "FLAC"

const val SND_FILE_NATIVE_LIB_NAME = "sndfile"

val FREETYPE_NATIVE_LIB_NAME = if (BuildConfig.DEBUG) "freetyped" else "freetype"

val GLBINDING_NATIVE_LIB_NAME = if (BuildConfig.DEBUG) "glbindingd" else "glbinding"

val PNG_NATIVE_LIB_NAME = if (BuildConfig.DEBUG) "png16d" else "png16"

const val NG_GL4ES_NATIVE_LIB_NAME = "ng_gl4es"

const val gl4esLibraryName = "gl4es"

val gl4esFullLibraryName = buildFullLibraryName(gl4esLibraryName)

val ngGL4ESFullLibraryName = buildFullLibraryName(NG_GL4ES_NATIVE_LIB_NAME)

fun buildFullLibraryName (libraryName : String ) = "lib${libraryName}.so"

val fteQWNativePlugins = arrayOf("fteplug_openssl","fteplug_bullet", "fteplug_cod", "fteplug_ezhud",
    "fteplug_ffmpeg", "fteplug_hl2", "fteplug_models", "fteplug_ode",
    "fteplug_qi", "fteplug_quake3")

val bulletLibs = arrayOf("Bullet3Common", "LinearMath", "BulletCollision", "BulletDynamics",
    "BulletInverseDynamics", "BulletSoftBody")

val angleLibs = arrayOf("feature_support_angle","GLESv2_angle", "EGL_angle")

val opensslLibs = arrayOf("ssl_3","crypto_3")

private val defaultFFMPEGLibs = arrayOf("avcodec", "avdevice", "avfilter", "avformat",
    "avutil", "swresample", "swscale")

private val armv7FFMPEGLibs = arrayOf("avcodec_neon", "avdevice_neon", "avfilter_neon", "avformat_neon",
    "avutil_neon", "swresample_neon", "swscale_neon")

fun getFMPEGLibs(context: Context) = if (context.applicationInfo.nativeLibraryDir.endsWith("arm"))
    armv7FFMPEGLibs else defaultFFMPEGLibs