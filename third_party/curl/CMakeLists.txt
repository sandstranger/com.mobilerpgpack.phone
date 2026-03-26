cmake_minimum_required(VERSION 4.1)
project(curl_builder)

include_directories(PUBLIC "${CMAKE_SOURCE_DIR}/../nghttp2/src/main/include")

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(WOLFSSL_LIBRARY "${CMAKE_SOURCE_DIR}/../wolfssl/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libwolfssl.so")
else ()
    set(WOLFSSL_LIBRARY "${CMAKE_SOURCE_DIR}/../wolfssl/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libwolfssl.so")
endif ()

set(WOLFSSL_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../wolfssl/src/main/include")

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(BROTLICOMMON_LIBRARY "${CMAKE_SOURCE_DIR}/../brotli/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libbrotlicommon.so")
    set(BROTLIDEC_LIBRARY "${CMAKE_SOURCE_DIR}/../brotli/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libbrotlidec.so")
else ()
    set(BROTLICOMMON_LIBRARY "${CMAKE_SOURCE_DIR}/../brotli/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libbrotlicommon.so")
    set(BROTLIDEC_LIBRARY "${CMAKE_SOURCE_DIR}/../brotli/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libbrotlidec.so")
endif ()

set(BROTLI_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../brotli/src/main/jni/brotli/c/include")

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(ZSTD_LIBRARY "${CMAKE_SOURCE_DIR}/../zstd/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libzstd.so")
else ()
    set(ZSTD_LIBRARY "${CMAKE_SOURCE_DIR}/../zstd/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libzstd.so")
endif ()

set(ZSTD_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../zstd/src/main/jni/zstd/lib")

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(NGHTTP2_LIBRARY "${CMAKE_SOURCE_DIR}/../nghttp2/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libnghttp2.so")
else ()
    set(NGHTTP2_LIBRARY "${CMAKE_SOURCE_DIR}/../nghttp2/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libnghttp2.so")
endif ()

set(NGHTTP2_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../nghttp2/src/main/jni/nghttp2/lib/includes")

add_subdirectory("src/main/jni/curl")