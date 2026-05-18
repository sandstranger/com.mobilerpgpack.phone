cmake_minimum_required(VERSION 4.1)
project(openal_builder)

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(OBOE_LIBRARY "${CMAKE_SOURCE_DIR}/../oboe/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/liboboe.so")
else ()
    set(OBOE_LIBRARY "${CMAKE_SOURCE_DIR}/../oboe/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/liboboe.so")
endif ()

set(OBOE_TARGET ${OBOE_LIBRARY})
set(SDL3_INCLUDE_DIRS "${CMAKE_SOURCE_DIR}/../sdl3/include")

add_subdirectory("src/main/jni/sdl2-compat")