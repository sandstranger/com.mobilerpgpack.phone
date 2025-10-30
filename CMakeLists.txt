cmake_minimum_required(VERSION 3.22.1)
project(Engines_Hub)

find_package(sdl3 REQUIRED CONFIG)
find_package(sdl2 REQUIRED CONFIG)
find_package(sdl2-mixer REQUIRED CONFIG)
find_package(sdl2-ttf REQUIRED CONFIG)
find_package(fmod REQUIRED CONFIG)
find_package(libpng REQUIRED CONFIG)
find_package(ZLIB REQUIRED)

set(SDL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/sdl2)
set(SDL2_INCLUDE_DIR ${SDL_LOCATION}/include)

if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(SDL2_LIBRARY ${SDL_LOCATION}/android-project/app/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2.so)
else()
    set(SDL2_LIBRARY ${SDL_LOCATION}/android-project/app/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2.so)
endif ()

set(OPENAL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/openal)
set(OPENAL_INCLUDE_DIR ${OPENAL_LOCATION}/src/main/jni/openal/include/)
include_directories(PUBLIC "${OPENAL_INCLUDE_DIR}/AL")

if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(OPENAL_LIBRARY ${OPENAL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libopenal.so)
else ()
    set(OPENAL_LIBRARY ${OPENAL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libopenal.so)
endif ()

if (NOT DEFINED LEGACY_GLES2 OR LEGACY_GLES2 STREQUAL "false")
    find_package(NG-GL4ES REQUIRED CONFIG)
    add_library(OPENGL_LIBRARY ALIAS NG-GL4ES::ng_gl4es)
else ()
set(OPENGL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/legacy_gl4es)
set(OPENGL_INCLUDE_DIR ${OPENGL_LOCATION}/src/main/jni/gl4es/include)
include_directories(PUBLIC "${OPENGL_INCLUDE_DIR}")
if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libgl4es.so)
    set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libgl4es.so)
else ()
    set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libgl4es.so)
    set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libgl4es.so)
endif ()
endif ()

set(SDL2_MIXER_DIR ${CMAKE_SOURCE_DIR}/third_party/sdl2-mixer/src/main/jni/SDL_mixer)
set(SDL2_MIXER_INCLUDE_DIR ${SDL2_MIXER_DIR}/include)
if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(SDL2_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-mixer/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_mixer.so)
else ()
    set(SDL2_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-mixer/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_mixer.so)
endif ()


include_directories(PUBLIC "${CMAKE_SOURCE_DIR}/third_party/fluidsynth/src/main/jni/fluidsynth-android/include")

if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(FLUIDSYNTH_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/fluidsynth/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libfluidsynth.so)
else ()
    set(FLUIDSYNTH_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/fluidsynth/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libfluidsynth.so)
endif ()

include_directories(PUBLIC "${CMAKE_SOURCE_DIR}/third_party/sdl2-ttf/src/main/jni/sdl-ttf")

if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(SDL2_TTF_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-ttf/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_ttf.so)
else ()
    set(SDL2_TTF_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-ttf/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_ttf.so)
endif ()

include_directories(PUBLIC "${CMAKE_SOURCE_DIR}/machine_translator/Translator/src/main/jni/include")

if (CMAKE_BUILD_TYPE EQUAL "DEBUG")
    set(TRANSLATOR_LIBRARY ${CMAKE_SOURCE_DIR}/machine_translator/Translator/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libTranslator.so)
else ()
    set(TRANSLATOR_LIBRARY ${CMAKE_SOURCE_DIR}/machine_translator/Translator/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libTranslator.so)
endif ()


#add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/WolfensteinRPG-RE")
#add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/DoomRPG-RE")
#add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/DoomIIRPG-RE")
add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/Doom64EX-Plus")

add_library(FMOD INTERFACE IMPORTED GLOBAL)
get_target_property(FMOD_IMPORTED_LOCATION fmod::fmod IMPORTED_LOCATION)
get_filename_component(FMOD_IMPORTED_LOCATION ${FMOD_IMPORTED_LOCATION} DIRECTORY)
target_link_libraries(FMOD INTERFACE ${FMOD_IMPORTED_LOCATION}/libfmod.so)

add_library(SDL3 INTERFACE IMPORTED GLOBAL)
get_target_property(SDL3_IMPORTED_LOCATION sdl3::SDL3-shared IMPORTED_LOCATION)
get_filename_component(SDL3_IMPORTED_LOCATION ${SDL3_IMPORTED_LOCATION} DIRECTORY)
target_link_libraries(SDL3 INTERFACE ${SDL3_IMPORTED_LOCATION}/libSDL3-shared.so)

target_link_libraries(
        "DOOM64"
        sdl3::SDL3-shared
        SDL3
        OPENGL_LIBRARY
        libpng::png_shared
        fmod::fmod
        FMOD
        z
        m
        log
)