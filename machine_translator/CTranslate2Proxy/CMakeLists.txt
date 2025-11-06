cmake_minimum_required(VERSION 4.1)
project(Ctranslate2Translator)

set(CMAKE_CXX_STANDARD 20)

add_library(CTranslate2Proxy SHARED)

set(CTRANSLATE2_INCLUDE_DIR ${CMAKE_SOURCE_DIR}/../../third_party/sentencepiece/src/main/jni/sentencepiece/src)
set(SENTENCE_PIECE_INCLUDE_DIR ${CMAKE_SOURCE_DIR}/../../third_party/CTranslate2/src/main/jni/CTranslate2/include)

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(CTRANSLATE2_LIBRARY ${CMAKE_SOURCE_DIR}/../../third_party/CTranslate2/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libctranslate2.so)
    set(SENTENCEPIECE_LIBRARY ${CMAKE_SOURCE_DIR}/../../third_party/sentencepiece/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libsentencepiece.so)
else()
    set(CTRANSLATE2_LIBRARY ${CMAKE_SOURCE_DIR}/../../third_party/CTranslate2/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libctranslate2.so)
    set(SENTENCEPIECE_LIBRARY ${CMAKE_SOURCE_DIR}/../../third_party/sentencepiece/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libsentencepiece.so)
endif()

target_include_directories(CTranslate2Proxy PRIVATE ${CTRANSLATE2_INCLUDE_DIR} ${SENTENCE_PIECE_INCLUDE_DIR})

target_sources(CTranslate2Proxy PRIVATE
        src/main/jni/src/OpusMtTranslator.cpp
        src/main/jni/src/M2M100Translator.cpp
        src/main/jni/src/Small100Translator.cpp
        src/main/jni/src/NLLB200Translator.cpp
        src/main/jni/src/Utils.cpp)

target_link_libraries(CTranslate2Proxy PRIVATE ${CTRANSLATE2_LIBRARY}
        ${SENTENCEPIECE_LIBRARY} log)
