cmake_minimum_required(VERSION 3.22.1)
project(doom-wolf-rpg)

add_link_options("LINKER:--build-id=none")

set(SDL_LOCATION ${CMAKE_SOURCE_DIR}/sdl2)
set(SDL2_INCLUDE_DIR ${SDL_LOCATION}/include)

if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(SDL2_LIBRARY ${SDL_LOCATION}/android-project/app/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2.so)
else()
    set(SDL2_LIBRARY ${SDL_LOCATION}/android-project/app/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2.so)
endif ()

set(OPENAL_LOCATION ${CMAKE_SOURCE_DIR}/openal)
set(OPENAL_INCLUDE_DIR ${OPENAL_LOCATION}/src/main/jni/openal/include/)
include_directories(PUBLIC "${OPENAL_INCLUDE_DIR}/AL")

if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(OPENAL_LIBRARY ${OPENAL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libopenal.so)
else ()
    set(OPENAL_LIBRARY ${OPENAL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libopenal.so)
endif ()

set(OPENGL_LOCATION ${CMAKE_SOURCE_DIR}/NG-GL4ES)
set(OPENGL_INCLUDE_DIR ${OPENGL_LOCATION}/src/main/jni/NG-GL4ES/include)
include_directories(PUBLIC "${OPENGL_INCLUDE_DIR}")
if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libng_gl4es.so)
    set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libng_gl4es.so)
else ()
    set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libng_gl4es.so)
    set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libng_gl4es.so)
endif ()

set(SDL2_MIXER_DIR ${CMAKE_SOURCE_DIR}/sdl2-mixer/src/main/jni/SDL_mixer)
set(SDL2_MIXER_INCLUDE_DIR ${SDL2_MIXER_DIR}/include)
if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(SDL2_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/sdl2-mixer/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_mixer.so)
else ()
    set(SDL2_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/sdl2-mixer/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_mixer.so)
endif ()


include_directories(PUBLIC "${CMAKE_SOURCE_DIR}/fluidsynth/src/main/jni/fluidsynth-android/include")

if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(FLUIDSYNTH_LIBRARY ${CMAKE_SOURCE_DIR}/fluidsynth/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libfluidsynth.so)
else ()
    set(FLUIDSYNTH_LIBRARY ${CMAKE_SOURCE_DIR}/fluidsynth/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libfluidsynth.so)
endif ()

include_directories(PUBLIC "${CMAKE_SOURCE_DIR}/sdl2-ttf/src/main/jni/sdl-ttf")

if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(SDL2_TTF_LIBRARY ${CMAKE_SOURCE_DIR}/sdl2-ttf/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_ttf.so)
else ()
    set(SDL2_TTF_LIBRARY ${CMAKE_SOURCE_DIR}/sdl2-ttf/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_ttf.so)
endif ()

include_directories(PUBLIC "${CMAKE_SOURCE_DIR}/Translator/src/main/jni/include")

if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(TRANSLATOR_LIBRARY ${CMAKE_SOURCE_DIR}/Translator/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libTranslator.so)
else ()
    set(TRANSLATOR_LIBRARY ${CMAKE_SOURCE_DIR}/Translator/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libTranslator.so)
endif ()

include_directories(PUBLIC "${CMAKE_SOURCE_DIR}/fmod/src/main/include")

add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/WolfensteinRPG-RE")
add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/DoomRPG-RE")
add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/DoomIIRPG-RE")
add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/Doom64EX-Plus")