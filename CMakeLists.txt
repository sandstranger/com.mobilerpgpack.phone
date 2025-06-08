cmake_minimum_required(VERSION 3.22.1)
project(doom-wolf-rpg)

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

set(OPENGL_LOCATION ${CMAKE_SOURCE_DIR}/gl4es)
set(OPENGL_INCLUDE_DIR ${OPENGL_LOCATION}/src/main/jni/gl4es/include)
include_directories(PUBLIC "${OPENGL_INCLUDE_DIR}")
if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libGL.so)
    set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libGL.so)
else ()
    set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libGL.so)
    set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libGL.so)
endif ()

set(SDL2_MIXER_DIR ${CMAKE_SOURCE_DIR}/sdl2-mixer/src/main/jni/SDL_mixer)
set(SDL2_MIXER_INCLUDE_DIR ${SDL2_MIXER_DIR}/include)
if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(SDL2_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/sdl2-mixer/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_mixer.so)
else ()
    set(SDL2_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/sdl2-mixer/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_mixer.so)
endif ()


set (FLUIDSYNTH_DIR ${CMAKE_SOURCE_DIR}/fluidsynth/src/main)
include_directories(PUBLIC "${CMAKE_SOURCE_DIR}/fluidsynth/src/main/include")
set(FLUIDSYNTH_LIBRARY ${CMAKE_SOURCE_DIR}/fluidsynth/src/main/jniLibs/${ANDROID_ABI}/libfluidsynth.so)


add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/WolfensteinRPG-RE")
add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/DoomRPG-RE")
add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/DoomIIRPG-RE")