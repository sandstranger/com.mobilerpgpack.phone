cmake_minimum_required(VERSION 4.1)
project(openal_builder)

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(OBOE_LIBRARY "${CMAKE_SOURCE_DIR}/../oboe/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/liboboe.so")
else ()
    set(OBOE_LIBRARY "${CMAKE_SOURCE_DIR}/../oboe/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/liboboe.so")
endif ()

set(OBOE_TARGET ${OBOE_LIBRARY})
set(OBOE_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../oboe/src/main/jni/oboe/include")

# No C++20 modules: the NDK's clang 21 crashes compiling OpenAL Soft's module build (a frontend
# segfault importing them, e.g. al/effects/chorus.cpp, alc/alconfig.cpp). The sources build the same
# from headers without them.
set(ALSOFT_ENABLE_MODULES OFF CACHE BOOL "Enable use of C++20 modules when supported" FORCE)

add_subdirectory("src/main/jni/openal")

target_include_directories(OpenAL PRIVATE ${OBOE_INCLUDE_DIR})