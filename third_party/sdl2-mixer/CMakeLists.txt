CMAKE_MINIMUM_REQUIRED(VERSION 3.22.1)

project(sdl_mixer_builder)

find_package(sdl2 REQUIRED CONFIG)
add_subdirectory(src/main/jni/SDL_mixer)

target_link_libraries("SDL2_mixer" PRIVATE sdl2::SDL2)