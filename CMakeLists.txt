cmake_minimum_required(VERSION 4.1)
project(Engines_Hub)

set(CMAKE_C_COMPILER_LAUNCHER ccache)
set(CMAKE_CXX_COMPILER_LAUNCHER ccache)

set(CMAKE_C_STANDARD 23)
set(CMAKE_C_STANDARD_REQUIRED ON)

set (CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS ON)

find_package(ZLIB REQUIRED)
find_package(games-frame-pacing REQUIRED CONFIG)

set(IS_DEBUG_BUILD OFF)
if(DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(IS_DEBUG_BUILD ON)
    add_compile_options(-fno-lto)
endif()

set(SAFFAL_LOCATION "${CMAKE_SOURCE_DIR}/third_party/SAFFAL/saffal" CACHE STRING "" FORCE)
set(SAFFAL_INCLUDE_DIR "${SAFFAL_LOCATION}/src/main/jni/include" CACHE STRING "" FORCE)

if (IS_DEBUG_BUILD)
    set(SAFFAL_LIBRARY "${SAFFAL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libsaffal.so"
            CACHE STRING "" FORCE)
else()
    set(SAFFAL_LIBRARY "${SAFFAL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libsaffal.so"
            CACHE STRING "" FORCE)
endif ()

set(SDL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/sdl2-compat CACHE STRING "" FORCE)
set(SDL2_INCLUDE_DIR "${SDL_LOCATION}/src/main/jni/sdl2-compat/include" CACHE STRING "" FORCE)

set(SDL2_INCLUDE_DIRS ${SDL2_INCLUDE_DIR} CACHE STRING "" FORCE)

if (IS_DEBUG_BUILD)
    set(SDL2_LIBRARY "${SDL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2.so"
            CACHE STRING "" FORCE)
else()
    set(SDL2_LIBRARY "${SDL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2.so"
            CACHE STRING "" FORCE)
endif ()

set(SDL2_LIBRARIES ${SDL2_LIBRARY} CACHE STRING "" FORCE)
set(SDL2_SDLMAIN_LIBRARY ${SDL2_LIBRARY} CACHE STRING "" FORCE)

set(OPENAL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/openal)
set(OPENAL_INCLUDE_DIR ${OPENAL_LOCATION}/src/main/jni/openal/include)

if (IS_DEBUG_BUILD)
    set(OPENAL_LIBRARY ${OPENAL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libopenal.so)
else ()
    set(OPENAL_LIBRARY ${OPENAL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libopenal.so)
endif ()

set(SDL2_MIXER_DIR ${CMAKE_SOURCE_DIR}/third_party/sdl2-mixer/src/main/jni/SDL_mixer)
set(SDL2_MIXER_INCLUDE_DIR ${SDL2_MIXER_DIR}/include)
if (IS_DEBUG_BUILD)
    set(SDL2_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-mixer/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_mixer.so)
else ()
    set(SDL2_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-mixer/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_mixer.so)
endif ()

set(SDL3_MIXER_DIR ${CMAKE_SOURCE_DIR}/third_party/sdl3-mixer/src/main/jni/SDL_mixer)
set(SDL3_MIXER_INCLUDE_DIR ${SDL3_MIXER_DIR}/include)
if (IS_DEBUG_BUILD)
    set(SDL3_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl3-mixer/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL3_mixer.so)
else ()
    set(SDL3_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl3-mixer/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL3_mixer.so)
endif ()

set(FLUIDSYNTH_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/fluidsynth/src/main/jni/fluidsynth-android/include")

if (IS_DEBUG_BUILD)
    set(FLUIDSYNTH_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/fluidsynth/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libfluidsynth.so)
else ()
    set(FLUIDSYNTH_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/fluidsynth/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libfluidsynth.so)
endif ()

set(SDL2_TTF_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/sdl2-ttf/src/main/jni/sdl-ttf")

if (IS_DEBUG_BUILD)
    set(SDL2_TTF_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-ttf/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_ttf.so)
else ()
    set(SDL2_TTF_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-ttf/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_ttf.so)
endif ()

set(TRANSLATOR_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/machine_translator/Translator/src/main/jni/include")

if (IS_DEBUG_BUILD)
    set(TRANSLATOR_LIBRARY ${CMAKE_SOURCE_DIR}/machine_translator/Translator/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libTranslator.so)
else ()
    set(TRANSLATOR_LIBRARY ${CMAKE_SOURCE_DIR}/machine_translator/Translator/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libTranslator.so)
endif ()


set(FMOD_INC_DIR "${CMAKE_SOURCE_DIR}/third_party/fmod/src/main/include")
set(FMOD_LIB "${CMAKE_SOURCE_DIR}/third_party/fmod/src/main/jniLibs/${ANDROID_ABI}/libfmod.so")

set(libpng_INCLUDE_DIRS "${CMAKE_SOURCE_DIR}/third_party/libpng/src/main/jni/libpng"
        "${CMAKE_SOURCE_DIR}/third_party/libpng/src/main/jni/include" CACHE STRING "" FORCE)

set(PNG_INCLUDE_DIRS ${libpng_INCLUDE_DIRS} CACHE STRING "" FORCE)

set(PNG_PNG_INCLUDE_DIR ${PNG_INCLUDE_DIRS} CACHE STRING "" FORCE)

if (IS_DEBUG_BUILD)
    set(libpng_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/libpng/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libpng16d.so)
else ()
    set(libpng_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/libpng/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libpng16.so)
endif ()

set(PNG_LIBRARY ${libpng_LIBRARY} CACHE STRING "" FORCE)

set(SDL3_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/sdl3/include")
if (IS_DEBUG_BUILD)
    set(SDL3_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl3/android-project/app/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL3.so)
else()
    set(SDL3_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl3/android-project/app/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL3.so)
endif ()

set(FREETYPE_INCLUDE_DIRS "${CMAKE_SOURCE_DIR}/third_party/freetype/src/main/jni/freetype/include" CACHE STRING "" FORCE)
if (IS_DEBUG_BUILD)
    set(FREETYPE_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/freetype/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libfreetyped.so")
else ()
    set(FREETYPE_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/freetype/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libfreetype.so")
endif ()

set(FREETYPE_LIBRARIES ${FREETYPE_LIBRARY} CACHE STRING "" FORCE)

set(JPEG_INCLUDE_DIRS "${CMAKE_SOURCE_DIR}/third_party/libjpegturbo/src/main/jni/libjpeg-turbo/src"
        "${CMAKE_SOURCE_DIR}/third_party/libjpegturbo/src/main/jni/include"
        CACHE STRING "" FORCE)
if (IS_DEBUG_BUILD)
    set(JPEG_LIBRARIES "${CMAKE_SOURCE_DIR}/third_party/libjpegturbo/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libjpeg.so" CACHE STRING "" FORCE)
else ()
    set(JPEG_LIBRARIES "${CMAKE_SOURCE_DIR}/third_party/libjpegturbo/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libjpeg.so" CACHE STRING "" FORCE)
endif ()

set(JPEG_INCLUDE_DIR "${JPEG_INCLUDE_DIRS}")
set(JPEG_LIBRARY "${JPEG_LIBRARIES}")

set(VORBISFILE_INCLUDE_DIRS "${CMAKE_SOURCE_DIR}/third_party/libvorbis/src/main/jni/libvorbis-android/include" CACHE STRING "" FORCE)
if (IS_DEBUG_BUILD)
    set(VORBISFILE_LIBRARIES "${CMAKE_SOURCE_DIR}/third_party/libvorbis/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libvorbis.so"
       "${CMAKE_SOURCE_DIR}/third_party/libvorbis/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libvorbisfile.so"
        "${CMAKE_SOURCE_DIR}/third_party/libvorbis/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libvorbisenc.so"
        CACHE STRING "" FORCE)
else ()
    set(VORBISFILE_LIBRARIES "${CMAKE_SOURCE_DIR}/third_party/libvorbis/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libvorbis.so"
     "${CMAKE_SOURCE_DIR}/third_party/libvorbis/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libvorbisenc.so"
     "${CMAKE_SOURCE_DIR}/third_party/libvorbis/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libvorbisfile.so"
     CACHE STRING "" FORCE)
endif ()


set(OGG_LOCATION ${CMAKE_SOURCE_DIR}/third_party/ogg)
set(OGG_INCLUDE_DIR ${OGG_LOCATION}/src/main/jni/ogg/include)
if (IS_DEBUG_BUILD)
    set(OGG_LIBRARY ${OGG_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libogg.so)
else ()
    set(OGG_LIBRARY ${OGG_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libogg.so)
endif ()

set(BULLET_INCLUDE_DIRS "${CMAKE_SOURCE_DIR}/third_party/bullet/src/main/jni/bullet3/src" CACHE STRING "" FORCE)
if (IS_DEBUG_BUILD)
    set(BULLET_LIBRARIES
            "${CMAKE_SOURCE_DIR}/third_party/bullet/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libLinearMath.so"
            "${CMAKE_SOURCE_DIR}/third_party/bullet/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libBulletDynamics.so"
            "${CMAKE_SOURCE_DIR}/third_party/bullet/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libBulletInverseDynamics.so"
            "${CMAKE_SOURCE_DIR}/third_party/bullet/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libBulletSoftBody.so"
            "${CMAKE_SOURCE_DIR}/third_party/bullet/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libBulletCollision.so"
            "${CMAKE_SOURCE_DIR}/third_party/bullet/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libBullet3Common.so"
            CACHE STRING "" FORCE)
else ()
    set(BULLET_LIBRARIES
            "${CMAKE_SOURCE_DIR}/third_party/bullet/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libBulletCollision.so"
            "${CMAKE_SOURCE_DIR}/third_party/bullet/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libBulletDynamics.so"
            "${CMAKE_SOURCE_DIR}/third_party/bullet/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libBulletInverseDynamics.so"
            "${CMAKE_SOURCE_DIR}/third_party/bullet/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libBulletSoftBody.so"
            "${CMAKE_SOURCE_DIR}/third_party/bullet/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libLinearMath.so"
            "${CMAKE_SOURCE_DIR}/third_party/bullet/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libBullet3Common.so"
            CACHE STRING "" FORCE)
endif ()

set(FFMPEG_INCLUDE_DIRS
        "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/FFmpeg"
        "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/include"
        CACHE STRING "" FORCE
)

set(FFMPEG_INCLUDE_DIR ${FFMPEG_INCLUDE_DIRS} CACHE STRING "" FORCE)

include_directories("${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/include" PUBLIC)
include_directories("${CMAKE_CURRENT_SOURCE_DIR}/third_party/sse2neon" PUBLIC)
include_directories(${SAFFAL_INCLUDE_DIR} PUBLIC)

if (${ANDROID_ABI} STREQUAL "armeabi-v7a")
    set(GNUTLS_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/jniLibs/${ANDROID_ABI}/libavformat_neon.so" CACHE STRING "" FORCE)
    set(AVFORMAT_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/jniLibs/${ANDROID_ABI}/libavformat_neon.so" CACHE STRING "" FORCE)
    set(AVCODEC_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/jniLibs/${ANDROID_ABI}/libavcodec_neon.so" CACHE STRING "" FORCE)
    set(AVUTIL_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/jniLibs/${ANDROID_ABI}/libavutil_neon.so" CACHE STRING "" FORCE)
    set(AVSWSCALE_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/jniLibs/${ANDROID_ABI}/libswscale_neon.so" CACHE STRING "" FORCE)
    set(SWRESAMPLE_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/jniLibs/${ANDROID_ABI}/libswresample_neon.so" CACHE STRING "" FORCE)
else()
    set(GNUTLS_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/jniLibs/${ANDROID_ABI}/libavformat.so" CACHE STRING "" FORCE)
    set(AVFORMAT_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/jniLibs/${ANDROID_ABI}/libavformat.so" CACHE STRING "" FORCE)
    set(AVCODEC_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/jniLibs/${ANDROID_ABI}/libavcodec.so" CACHE STRING "" FORCE)
    set(AVUTIL_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/jniLibs/${ANDROID_ABI}/libavutil.so" CACHE STRING "" FORCE)
    set(AVSWSCALE_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/jniLibs/${ANDROID_ABI}/libswscale.so" CACHE STRING "" FORCE)
    set(SWRESAMPLE_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ffmpeg/src/main/jniLibs/${ANDROID_ABI}/libswresample.so" CACHE STRING "" FORCE)
endif()

set(FFMPEG_AVCODEC_LIBRARY ${AVCODEC_LIBRARY} CACHE STRING "" FORCE)
set(FFMPEG_AVFORMAT_LIBRARY ${AVFORMAT_LIBRARY} CACHE STRING "" FORCE)
set(FFMPEG_AVUTIL_LIBRARY ${AVUTIL_LIBRARY} CACHE STRING "" FORCE)
set(FFMPEG_SWSCALE_LIBRARY ${AVSWSCALE_LIBRARY} CACHE STRING "" FORCE)
set(FFMPEG_SWRESAMPLE_LIBRARY ${SWRESAMPLE_LIBRARY} CACHE STRING "" FORCE)

set(VPX_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/vpx/android/${ANDROID_ABI}/include")
set(VPX_LIBRARIES "${CMAKE_SOURCE_DIR}/third_party/vpx/android/${ANDROID_ABI}/lib/libvpx.a")

set(ZMUSIC_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/zmusic/src/main/jni/ZMusic/include")
set(ZMUSIC_FOUND TRUE)
if (IS_DEBUG_BUILD)
    set(ZMUSIC_LIBRARIES "${CMAKE_SOURCE_DIR}/third_party/zmusic/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libzmusic.so")
else ()
    set(ZMUSIC_LIBRARIES "${CMAKE_SOURCE_DIR}/third_party/zmusic/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libzmusic.so")
endif ()

set(RE2C_EXECUTABLE "${CMAKE_SOURCE_DIR}/third_party/uzdoom_tools/re2c.exe")
set(LEMON_EXECUTABLE "${CMAKE_SOURCE_DIR}/third_party/uzdoom_tools/lemon.exe")

set(BZIP2_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/bzip2/src/main/jni/bzip2" CACHE STRING "" FORCE)

if (IS_DEBUG_BUILD)
    set(BZIP2_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/bzip2/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libbz2.so"
    CACHE STRING "bzip2 library"
    FORCE)
else ()
    set(BZIP2_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/bzip2/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libbz2.so"
    CACHE STRING "bzip2 library"
    FORCE)
endif ()

set(ODE_INCLUDE_DIRS "${CMAKE_SOURCE_DIR}/third_party/ode/src/main/jni/ODE/include" CACHE STRING "" FORCE)

if (IS_DEBUG_BUILD)
    set(LIBODE_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ode/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libode.so"
    CACHE STRING ""
    FORCE)
else ()
    set(LIBODE_LIBRARY "${CMAKE_SOURCE_DIR}/third_party/ode/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libode.so"
    CACHE STRING ""
    FORCE)
endif ()

set(SDL2_IMAGE_LOCATION ${CMAKE_SOURCE_DIR}/third_party/sdl2-image CACHE STRING "" FORCE)

set(SDL2_IMAGE_INCLUDE_DIRS ${SDL2_IMAGE_LOCATION}/src/main/jni/SDL_image/include CACHE STRING "" FORCE)

if (IS_DEBUG_BUILD)
    set(SDL2_IMAGE_LIBRARIES
            ${SDL2_IMAGE_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_image.so
            CACHE STRING "" FORCE)
else()
    set(SDL2_IMAGE_LIBRARIES
            ${SDL2_IMAGE_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_image.so
            CACHE STRING "" FORCE)
endif ()

set(OpenSSL_INCLUDE_DIR ${CMAKE_SOURCE_DIR}/third_party/openssl/src/main/include CACHE STRING "" FORCE)
set(OpenSSL_LIBRARY
            ${CMAKE_SOURCE_DIR}/third_party/openssl/src/main/jniLibs/${ANDROID_ABI}/libcrypto_3.so
            ${CMAKE_SOURCE_DIR}/third_party/openssl/src/main/jniLibs/${ANDROID_ABI}/libssl_3.so
            CACHE STRING "" FORCE)

set(ICU_INCLUDE_DIR ${CMAKE_SOURCE_DIR}/third_party/icu4c/include CACHE STRING "" FORCE)

set(ICU_LIBRARIES_FOLDER_LOCATION ${CMAKE_SOURCE_DIR}/third_party/icu4c/libs/android CACHE STRING "" FORCE)

set(ICU_LIBRARIES
        ${ICU_LIBRARIES_FOLDER_LOCATION}/${ANDROID_ABI}/libicudata_floris.a
        ${ICU_LIBRARIES_FOLDER_LOCATION}/${ANDROID_ABI}/libicuuc_floris.a
        CACHE STRING "" FORCE)

set(GLBINDING_INCLUDES ${CMAKE_SOURCE_DIR}/third_party/glbinding/src/main/jni/glbinding/source/glbinding/include
        ${CMAKE_SOURCE_DIR}/third_party/glbinding/src/main/jni/include)

if (IS_DEBUG_BUILD)
    set(GLBINDING_LIBRARIES
            ${CMAKE_SOURCE_DIR}/third_party/glbinding/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libglbindingd.so
            CACHE STRING "" FORCE)
else()
    set(GLBINDING_LIBRARIES
            ${CMAKE_SOURCE_DIR}/third_party/glbinding/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libglbinding.so
            CACHE STRING "" FORCE)
endif ()

set(GLOB_INCLUDE_DIR ${CMAKE_SOURCE_DIR}/third_party/glob/src/main/jni/glob)

if (IS_DEBUG_BUILD)
    set(GLOB_LIBRARY
            ${CMAKE_SOURCE_DIR}/third_party/glob/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libglob.so
            CACHE STRING "" FORCE)
else()
    set(GLOB_LIBRARY
            ${CMAKE_SOURCE_DIR}/third_party/glob/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libglob.so
            CACHE STRING "" FORCE)
endif ()

set(asio_location ${CMAKE_SOURCE_DIR}/third_party/asio/asio/include CACHE STRING "" FORCE)

include_directories ("${OGG_LOCATION}/src/main/include")

set (USE_OPENGL ON)
set (WITH_SDL "2")
set (BUILD_TOOLS OFF)
set (INSTALL_BLENDER_PLUGIN OFF)
set (default_DEBUG OFF)
set (Boost_VERSION "1.91.0")
set(USE_LTO OFF)
set(USE_LD "lld")
set(SET_OPTIMIZATION_FLAGS ON CACHE BOOL "" FORCE)
set(DEBUG_EXTRA OFF)
set(USE_CXX17_FROM_CHARS_FLOAT ON)
set(CXX_STD_VERSION 2020)
set(SET_WARNING_FLAGS OFF)
set(Boost_INCLUDE_DIRS "${CMAKE_SOURCE_DIR}/third_party/boost")
set(GLM_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/glm")
set(OPENAL_EFX_INCLUDE_DIR "${OPENAL_INCLUDE_DIR}/AL")

set(FTE_DEP_ZLIB true CACHE BOOL "" FORCE)
set(FTE_DEP_BZIP2 true CACHE BOOL "" FORCE)
set(FTE_TOOL_IMAGE false CACHE BOOL "" FORCE)
set(FTE_TOOL_IQM false CACHE BOOL "" FORCE)
set(FTE_TOOL_QTV false CACHE BOOL "" FORCE)
set(FTE_TOOL_HTTPSV false CACHE BOOL "" FORCE)
set(FTE_TOOL_QCVM false CACHE BOOL "" FORCE)
set(FTE_TOOL_QCC false CACHE BOOL "" FORCE)
set(FTE_TOOL_QCCGUI false CACHE BOOL "" FORCE)
set(FTE_PLUG_IRC false CACHE BOOL "" FORCE)
set(FTE_DEP_GNUTLS false CACHE BOOL "" FORCE)
set(FTE_PLUG_HL2 true CACHE BOOL "" FORCE)
set(FTE_PLUG_MODELS true CACHE BOOL "" FORCE)
set(FTE_PLUG_OPENXR false CACHE BOOL "" FORCE)
set(FTE_PLUG_CEF false CACHE BOOL "" FORCE)
set(FTE_PLUG_XMPP false CACHE BOOL "" FORCE)
set(FTE_PLUG_ODE true CACHE BOOL "" FORCE)
set(FTE_DEP_SDL3 false CACHE BOOL "" FORCE)
set(FTE_USE_SDL true CACHE BOOL "" FORCE)
if(CMAKE_CXX_BYTE_ORDER EQUAL BIG_ENDIAN)
    set(FTE_LITTLE_ENDIAN false CACHE BOOL "" FORCE)
    set(FTE_BIG_ENDIAN true CACHE BOOL "" FORCE)
else()
    set(FTE_LITTLE_ENDIAN true CACHE BOOL "" FORCE)
    set(FTE_BIG_ENDIAN false CACHE BOOL "" FORCE)
endif()

set(USE_XDG ON CACHE BOOL "" FORCE)
set(OPTION_BUILD_CODECHECK OFF CACHE BOOL "" FORCE)
set(OPTION_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(OPTION_USE_GLBINDING ON CACHE BOOL "" FORCE)
set(OPTION_FORCE_EMBEDDED_MINIZIP ON CACHE BOOL "" FORCE)
set(OPTION_BUILD_WEBSITE_TOOLS OFF CACHE BOOL "" FORCE)

set(USE_PRECOMPILED_HEADERS OFF CACHE BOOL "" FORCE)
set(USE_SYSTEM_ZLIB ON CACHE BOOL "" FORCE)
set(USE_SYSTEM_LIBGLEW ON CACHE BOOL "" FORCE)
set(USE_SYSTEM_LIBJPEG ON CACHE BOOL "" FORCE)
set(MONOLITH ON CACHE BOOL "" FORCE)
set(MAKE_DLL OFF CACHE BOOL "" FORCE)

set(APP_ROOT_DIR "${CMAKE_CURRENT_SOURCE_DIR}/app/src/main/jni" CACHE STRING "" FORCE)
include_directories("${APP_ROOT_DIR}/AndroidGraphicsLayer/include")

add_subdirectory("${APP_ROOT_DIR}/AndroidGraphicsLayer")
add_subdirectory("${APP_ROOT_DIR}/WolfensteinRPG-RE")
add_subdirectory("${APP_ROOT_DIR}/DoomRPG-RE")
add_subdirectory("${APP_ROOT_DIR}/DoomIIRPG-RE")
add_subdirectory("${APP_ROOT_DIR}/Doom64EX-Plus")
add_subdirectory("${APP_ROOT_DIR}/DOOM64-EX-Plus-Enhanced")
add_subdirectory("${APP_ROOT_DIR}/PsyDoom")
add_subdirectory("${APP_ROOT_DIR}/UZDoom")
add_subdirectory("${APP_ROOT_DIR}/ArxLibertatis")
add_subdirectory("${APP_ROOT_DIR}/fteqw")
add_subdirectory("${APP_ROOT_DIR}/widelands")
add_subdirectory("${APP_ROOT_DIR}/Vanilla-Conquer")
add_subdirectory("${APP_ROOT_DIR}/Classic-RBDOOM-3-BFG/neo")

set(ROM_CONFIGS jpn-final ntsc-final pal-final)

foreach(ROM_ID IN LISTS ROM_CONFIGS)
    set(BUILD_DIR "${CMAKE_CURRENT_BINARY_DIR}/build_${ROM_ID}")
    file(MAKE_DIRECTORY ${BUILD_DIR})
    set(CACHE_FILE "${BUILD_DIR}/CMakeCache.txt")
    set(ROMID ${ROM_ID} CACHE STRING "ROM ID" FORCE)
    add_subdirectory("${APP_ROOT_DIR}/perfect_dark" "${BUILD_DIR}")
endforeach()

target_include_directories(arx PRIVATE "${OPENAL_INCLUDE_DIR}/AL")

target_include_directories(DoomRPG PRIVATE ${SDL2_INCLUDE_DIR} ${SDL2_MIXER_INCLUDE_DIR}
${SDL2_TTF_INCLUDE_DIR} ${TRANSLATOR_INCLUDE_DIR} ${FLUIDSYNTH_INCLUDE_DIR})

target_link_libraries(DoomRPG PRIVATE ${SAFFAL_LIBRARY} ${SDL2_LIBRARY} ${SDL2_MIXER_LIBRARY}
        ${SDL2_TTF_LIBRARY} ZLIB::ZLIB ${TRANSLATOR_LIBRARY} ${FLUIDSYNTH_LIBRARY})

target_include_directories(DoomIIRPG PRIVATE "${OPENAL_INCLUDE_DIR}/AL" ${SDL2_INCLUDE_DIR}
        ${SDL2_TTF_INCLUDE_DIR} ${TRANSLATOR_INCLUDE_DIR})

target_link_libraries(DoomIIRPG PRIVATE ${SAFFAL_LIBRARY} ${SDL2_LIBRARY} ${SDL2_TTF_LIBRARY} ZLIB::ZLIB
        ${OPENAL_LIBRARY} ${TRANSLATOR_LIBRARY} AndroidGraphicsLayer)

target_include_directories(WolfensteinRPG PRIVATE "${OPENAL_INCLUDE_DIR}" ${SDL2_INCLUDE_DIR}
        ${SDL2_TTF_INCLUDE_DIR} ${TRANSLATOR_INCLUDE_DIR})

target_link_libraries(WolfensteinRPG PRIVATE ${SAFFAL_LIBRARY} ${SDL2_LIBRARY} ${SDL2_TTF_LIBRARY} ZLIB::ZLIB
         ${OPENAL_LIBRARY} ${TRANSLATOR_LIBRARY} AndroidGraphicsLayer)

target_include_directories(DOOM64 PRIVATE ${SDL3_INCLUDE_DIR} ${libpng_INCLUDE_DIRS}
        ${FMOD_INC_DIR})

target_link_libraries(DOOM64 PRIVATE ${SAFFAL_LIBRARY} ${SDL3_LIBRARY} ${libpng_LIBRARY}
        ${FMOD_LIB} ZLIB::ZLIB m log AndroidGraphicsLayer)

target_include_directories(DOOM64-Enhanced PRIVATE ${SDL3_INCLUDE_DIR} ${libpng_INCLUDE_DIRS}
        ${FMOD_INC_DIR})

target_link_libraries(DOOM64-Enhanced PRIVATE ${SAFFAL_LIBRARY} ${SDL3_LIBRARY} ${libpng_LIBRARY}
        ${FMOD_LIB} ZLIB::ZLIB m log AndroidGraphicsLayer)

target_include_directories(PsyDoom PRIVATE ${SDL2_INCLUDE_DIR})
target_link_libraries(PsyDoom PRIVATE ${SAFFAL_LIBRARY} ${SDL2_LIBRARY})

target_include_directories(VulkanGL PRIVATE ${SDL2_INCLUDE_DIR})
target_link_libraries(VulkanGL PRIVATE ${SDL2_LIBRARY})