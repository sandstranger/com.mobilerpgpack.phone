cmake_minimum_required(VERSION 3.22.1)
project(fmod)

set(FMOD_LIB_PATH "${CMAKE_CURRENT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libfmod.so")

# Create a dummy source file (create an empty one if it doesn't exist)
add_library(fmod SHARED "${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/dummy.cpp")

# After building the dummy target, overwrite it with the real library
add_custom_command(TARGET fmod POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy ${FMOD_LIB_PATH} $<TARGET_FILE:fmod>
        COMMENT "Replacing dummy library with the real libfmod.so"
)