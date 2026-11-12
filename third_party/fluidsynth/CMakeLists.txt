cmake_minimum_required(VERSION 4.1)
project(fluidsynth_builder)

set(enable-oboe ON CACHE BOOL "" FORCE)
set(enable-opensles OFF CACHE BOOL "" FORCE)

# Oboe is the only output on Android, as in the old fork. The NDK has <sys/soundcard.h>, so
# upstream compiled the OSS driver in; it comes before Oboe in fluidsynth's driver list, became
# the default audio.driver and failed on /dev/dsp (there is none on Android): no MIDI music.
set(enable-oss OFF CACHE BOOL "" FORCE)
set(enable-alsa OFF CACHE BOOL "" FORCE)
set(enable-jack OFF CACHE BOOL "" FORCE)
set(enable-pulseaudio OFF CACHE BOOL "" FORCE)
set(enable-pipewire OFF CACHE BOOL "" FORCE)
set(enable-portaudio OFF CACHE BOOL "" FORCE)
set(enable-sdl3 OFF CACHE BOOL "" FORCE)
set(enable-midishare OFF CACHE BOOL "" FORCE)
set(enable-dbus OFF CACHE BOOL "" FORCE)
set(enable-readline OFF CACHE BOOL "" FORCE)
# the "file" driver writes fluidsynth.wav, never an output here
set(enable-aufile OFF CACHE BOOL "" FORCE)

set(enable-openmp OFF CACHE BOOL "" FORCE)

# float DSP on the 32 bit ABIs like the fork: ARMv7 NEON has no double precision
if (ANDROID_ABI STREQUAL "armeabi-v7a" OR ANDROID_ABI STREQUAL "x86")
    set(enable-floats ON CACHE BOOL "" FORCE)
else ()
    set(enable-floats OFF CACHE BOOL "" FORCE)
endif ()

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(_oboe_library  "${CMAKE_CURRENT_SOURCE_DIR}/../oboe/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/liboboe.so"
            CACHE PATH "" FORCE)
else ()
    set(_oboe_library "${CMAKE_CURRENT_SOURCE_DIR}/../oboe/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/liboboe.so"
            CACHE PATH "" FORCE)
endif ()

set(oboe_INCLUDE_DIR "${CMAKE_CURRENT_SOURCE_DIR}/../oboe/src/main/jni/oboe/include"
        CACHE PATH "" FORCE)

include_directories("${CMAKE_CURRENT_SOURCE_DIR}/../oboe/src/main/jni/oboe/include")

add_subdirectory("src/main/jni/fluidsynth-android")
