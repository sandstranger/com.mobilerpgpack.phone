cmake_minimum_required(VERSION 4.1)
project(vorbis_builder)

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(OGG_LIBRARY "${CMAKE_SOURCE_DIR}/../ogg/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libogg.so")
else ()
    set(OGG_LIBRARY "${CMAKE_SOURCE_DIR}/../ogg/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libogg.so")
endif ()

set(OGG_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../ogg/src/main/jni/ogg/include")

include_directories(${OGG_INCLUDE_DIR} "${CMAKE_SOURCE_DIR}/../ogg/src/main/include")

add_subdirectory("src/main/jni/libvorbis-android")

