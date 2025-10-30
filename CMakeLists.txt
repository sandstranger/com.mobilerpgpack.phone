cmake_minimum_required(VERSION 3.22.1)
project(Engines_Hub)

find_package(sdl3 REQUIRED CONFIG)
find_package(sdl2 REQUIRED CONFIG)
find_package(sdl2-mixer REQUIRED CONFIG)
find_package(sdl2-ttf REQUIRED CONFIG)
find_package(fmod REQUIRED CONFIG)
find_package(libpng REQUIRED CONFIG)
find_package(OpenAL REQUIRED CONFIG)
find_package(fluidsynth REQUIRED CONFIG)
find_package(Translator REQUIRED CONFIG)
find_package(ZLIB REQUIRED)

set(SDL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/sdl2)
set(SDL2_INCLUDE_DIR ${SDL_LOCATION}/include)

if (NOT DEFINED LEGACY_GLES2 OR LEGACY_GLES2 STREQUAL "false")
    find_package(NG-GL4ES REQUIRED CONFIG)
    add_library(OPENGL_LIBRARY ALIAS NG-GL4ES::ng_gl4es)
else ()
    find_package(legacy_gl4es REQUIRED CONFIG)
    add_library(OPENGL_LIBRARY ALIAS legacy_gl4es::gl4es)
endif ()

add_library(FMOD INTERFACE IMPORTED GLOBAL)
get_target_property(FMOD_IMPORTED_LOCATION fmod::fmod IMPORTED_LOCATION)
get_filename_component(FMOD_IMPORTED_LOCATION ${FMOD_IMPORTED_LOCATION} DIRECTORY)
target_link_libraries(FMOD INTERFACE ${FMOD_IMPORTED_LOCATION}/libfmod.so)

add_library(SDL3 INTERFACE IMPORTED GLOBAL)
get_target_property(SDL3_IMPORTED_LOCATION sdl3::SDL3-shared IMPORTED_LOCATION)
get_filename_component(SDL3_IMPORTED_LOCATION ${SDL3_IMPORTED_LOCATION} DIRECTORY)
target_link_libraries(SDL3 INTERFACE ${SDL3_IMPORTED_LOCATION}/libSDL3-shared.so)

add_library(FLUIDSYNTH INTERFACE IMPORTED GLOBAL)
get_target_property(FLUIDSYNTH_IMPORTED_LOCATION fluidsynth::fluidsynth IMPORTED_LOCATION)
get_filename_component(FLUIDSYNTH_IMPORTED_LOCATION ${FLUIDSYNTH_IMPORTED_LOCATION} DIRECTORY)
target_link_libraries(FLUIDSYNTH INTERFACE ${FLUIDSYNTH_IMPORTED_LOCATION}/libfluidsynth.so)

#add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/WolfensteinRPG-RE")
add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/DoomRPG-RE")
#add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/DoomIIRPG-RE")
add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/Doom64EX-Plus")

target_link_libraries("DoomRPG" PRIVATE sdl2::SDL2 sdl2-mixer::SDL2_mixer
        sdl2-ttf::SDL2_ttf ZLIB::ZLIB Translator::Translator fluidsynth::fluidsynth FLUIDSYNTH)

target_link_libraries(
        "DOOM64"
        PRIVATE
        sdl3::SDL3-shared
        SDL3
        OPENGL_LIBRARY
        libpng::png_shared
        fmod::fmod
        FMOD
        ZLIB::ZLIB
        m
        log
)