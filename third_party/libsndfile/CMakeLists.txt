cmake_minimum_required(VERSION 4.1)
project(sndfile_builder)

set(IS_DEBUG_BUILD OFF)
if(DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(IS_DEBUG_BUILD ON)
endif()

if (IS_DEBUG_BUILD)
    set(OGG_LIBRARY "${CMAKE_SOURCE_DIR}/../ogg/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libogg.so")
else ()
    set(OGG_LIBRARY "${CMAKE_SOURCE_DIR}/../ogg/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libogg.so")
endif ()

set(OGG_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../ogg/src/main/jni/ogg/include")
include_directories(${OGG_INCLUDE_DIR} "${CMAKE_SOURCE_DIR}/../ogg/src/main/include")

if (IS_DEBUG_BUILD)
    set(FLAC_LIBRARY "${CMAKE_SOURCE_DIR}/../flac/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libFLAC.so")
else ()
    set(FLAC_LIBRARY "${CMAKE_SOURCE_DIR}/../flac/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libFLAC.so")
endif ()

set(FLAC_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../flac/src/main/jni/flac/include")

if (IS_DEBUG_BUILD)
    set(MP3LAME_LIBRARY "${CMAKE_SOURCE_DIR}/../libmp3lame/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libmp3lame.so")
    set(MP3LAME_HIP_LIBRARY "${CMAKE_SOURCE_DIR}/../libmp3lame/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libmp3lame.so")
else ()
    set(MP3LAME_LIBRARY "${CMAKE_SOURCE_DIR}/../libmp3lame/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libmp3lame.so")
    set(MP3LAME_HIP_LIBRARY "${CMAKE_SOURCE_DIR}/../libmp3lame/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libmp3lame.so")
endif ()

set(MP3LAME_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../libmp3lame/src/main/jni/libmp3lame-android/jni/include")

if (IS_DEBUG_BUILD)
    set(mpg123_LIBRARY "${CMAKE_SOURCE_DIR}/../mpg123/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libmpg123.so")
else ()
    set(mpg123_LIBRARY "${CMAKE_SOURCE_DIR}/../mpg123/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libmpg123.so")
endif ()

set(mpg123_INCLUDE_DIR_Android "${CMAKE_SOURCE_DIR}/../mpg123/src/main/jni/MPG123-Android/src/libmpg123")
set(mpg123_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../mpg123/src/main/jni/MPG123-Android/android")

if (IS_DEBUG_BUILD)
    set(Vorbis_Vorbis_LIBRARY "${CMAKE_SOURCE_DIR}/../libvorbis/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libvorbis.so")
    set(Vorbis_Enc_LIBRARY "${CMAKE_SOURCE_DIR}/../libvorbis/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libvorbisenc.so")
    set(Vorbis_File_LIBRARY "${CMAKE_SOURCE_DIR}/../libvorbis/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libvorbisfile.so")
else ()
    set(Vorbis_Vorbis_LIBRARY "${CMAKE_SOURCE_DIR}/../libvorbis/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libvorbis.so")
    set(Vorbis_Enc_LIBRARY "${CMAKE_SOURCE_DIR}/../libvorbis/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libvorbisenc.so")
    set(Vorbis_File_LIBRARY "${CMAKE_SOURCE_DIR}/../libvorbis/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libvorbisfile.so")
endif ()

if (IS_DEBUG_BUILD)
    set(SAFFAL_LIBRARY "${CMAKE_CURRENT_SOURCE_DIR}/../SAFFAL/saffal/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libsaffal.so")
else ()
    set(SAFFAL_LIBRARY "${CMAKE_CURRENT_SOURCE_DIR}/../SAFFAL/saffal/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libsaffal.so")
endif ()

set(Vorbis_Vorbis_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../libvorbis/src/main/jni/libvorbis-android/include")

if (IS_DEBUG_BUILD)
    set(OPUS_LIBRARY "${CMAKE_SOURCE_DIR}/../opus/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libopus.so")
else ()
    set(OPUS_LIBRARY "${CMAKE_SOURCE_DIR}/../opus/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libopus.so")
endif ()

set(OPUS_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/../opus/src/main/jni/opus/include")

add_subdirectory("src/main/jni/libsndfile")

target_include_directories(sndfile PRIVATE ${OPUS_INCLUDE_DIR} ${OGG_INCLUDE_DIR}
        ${FLAC_INCLUDE_DIR} ${MP3LAME_INCLUDE_DIR} ${mpg123_INCLUDE_DIR}
        ${mpg123_INCLUDE_DIR_Android} ${Vorbis_Vorbis_INCLUDE_DIR})

target_link_libraries(sndfile PRIVATE ${SAFFAL_LIBRARY})