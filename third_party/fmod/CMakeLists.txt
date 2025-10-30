cmake_minimum_required(VERSION 3.22.1)
project(fmod)

set(FMOD_LIB_PATH "${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libfmod.so")

add_library(fmod SHARED "${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/dummy.cpp")

add_custom_command(TARGET fmod POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy ${FMOD_LIB_PATH} $<TARGET_FILE:fmod>
        COMMENT "Replacing dummy library with the real libfmod.so"
)