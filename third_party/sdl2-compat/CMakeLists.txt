cmake_minimum_required(VERSION 4.1)
project(sdl2_compat_builder)
set(SDL3_INCLUDE_DIRS "${CMAKE_SOURCE_DIR}/../sdl3/include")
add_subdirectory("src/main/jni/sdl2-compat")