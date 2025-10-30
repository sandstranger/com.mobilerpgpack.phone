CMAKE_MINIMUM_REQUIRED(VERSION 3.22.1)

project(sdl_mixer_builder)

find_package(sdl2 REQUIRED CONFIG)
add_subdirectory(src/main/jni/sdl-ttf)

target_link_libraries("SDL2_ttf" PRIVATE sdl2::SDL2)