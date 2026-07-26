cmake_minimum_required(VERSION 4.1)
project(glob)

set(CMAKE_C_STANDARD 23)
set(CMAKE_C_STANDARD_REQUIRED ON)
set(CMAKE_C_EXTENSIONS ON)

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(SAFFAL_LIBRARY "${CMAKE_CURRENT_SOURCE_DIR}/../SAFFAL/saffal/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libsaffal.so")
else ()
    set(SAFFAL_LIBRARY "${CMAKE_CURRENT_SOURCE_DIR}/../SAFFAL/saffal/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libsaffal.so")
endif ()

add_library(glob SHARED)
target_include_directories(glob PRIVATE /src/main/jni/glob)
target_sources(glob PRIVATE src/main/jni/glob/glob.c)
target_link_libraries(glob ${SAFFAL_LIBRARY})