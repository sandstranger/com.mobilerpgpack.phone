cmake_minimum_required(VERSION 4.1)
project(openal_builder)

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(OBOE_LIBRARY "${CMAKE_SOURCE_DIR}/../oboe/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/liboboe.so")
else ()
    set(OBOE_LIBRARY "${CMAKE_SOURCE_DIR}/../oboe/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/liboboe.so")
endif ()

set(OBOE_TARGET ${OBOE_LIBRARY})
set(OBOE_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../oboe/src/main/jni/oboe/include")

add_subdirectory("src/main/jni/openal")

target_include_directories(OpenAL PRIVATE ${OBOE_INCLUDE_DIR})