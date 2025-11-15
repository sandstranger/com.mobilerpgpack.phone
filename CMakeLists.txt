cmake_minimum_required(VERSION 4.1)
project(Engines_Hub)

set(CMAKE_C_STANDARD 23)
set(CMAKE_C_STANDARD_REQUIRED ON)

find_package(ZLIB REQUIRED)

set(IS_DEBUG_BUILD OFF)
if(DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(IS_DEBUG_BUILD ON)
endif()

set(SDL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/sdl2)
set(SDL2_INCLUDE_DIR ${SDL_LOCATION}/include)

if (IS_DEBUG_BUILD)
    set(SDL2_LIBRARY ${SDL_LOCATION}/android-project/app/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2.so)
else()
    set(SDL2_LIBRARY ${SDL_LOCATION}/android-project/app/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2.so)
endif ()

set(OPENAL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/openal)
set(OPENAL_INCLUDE_DIR ${OPENAL_LOCATION}/src/main/jni/openal/include)

if (IS_DEBUG_BUILD)
    set(OPENAL_LIBRARY ${OPENAL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libopenal.so)
else ()
    set(OPENAL_LIBRARY ${OPENAL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libopenal.so)
endif ()

if (NOT DEFINED LEGACY_GLES2 OR LEGACY_GLES2 STREQUAL "false")
    set(OPENGL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/NG-GL4ES)
    set(OPENGL_INCLUDE_DIR ${OPENGL_LOCATION}/src/main/jni/NG-GL4ES/include)
    if (IS_DEBUG_BUILD)
        set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libng_gl4es.so)
        set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libng_gl4es.so)
    else ()
        set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libng_gl4es.so)
        set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libng_gl4es.so)
    endif ()
else ()
    set(OPENGL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/legacy_gl4es)
    set(OPENGL_INCLUDE_DIR ${OPENGL_LOCATION}/src/main/jni/gl4es/include)
    if (IS_DEBUG_BUILD)
        set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libgl4es.so)
        set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libgl4es.so)
    else ()
        set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libgl4es.so)
        set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libgl4es.so)
    endif ()
endif ()

set(SDL2_MIXER_DIR ${CMAKE_SOURCE_DIR}/third_party/sdl2-mixer/src/main/jni/SDL_mixer)
set(SDL2_MIXER_INCLUDE_DIR ${SDL2_MIXER_DIR}/include)
if (IS_DEBUG_BUILD)
    set(SDL2_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-mixer/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_mixer.so)
else ()
    set(SDL2_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-mixer/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_mixer.so)
endif ()

set(FLUIDSYNTH_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/fluidsynth/src/main/jni/fluidsynth-android/include")

if (IS_DEBUG_BUILD)
    set(FLUIDSYNTH_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/fluidsynth/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libfluidsynth.so)
else ()
    set(FLUIDSYNTH_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/fluidsynth/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libfluidsynth.so)
endif ()

set(SDL2_TTF_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/sdl2-ttf/src/main/jni/sdl-ttf")

if (IS_DEBUG_BUILD)
    set(SDL2_TTF_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-ttf/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_ttf.so)
else ()
    set(SDL2_TTF_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-ttf/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_ttf.so)
endif ()

set(TRANSLATOR_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/machine_translator/Translator/src/main/jni/include")

if (IS_DEBUG_BUILD)
    set(TRANSLATOR_LIBRARY ${CMAKE_SOURCE_DIR}/machine_translator/Translator/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libTranslator.so)
else ()
    set(TRANSLATOR_LIBRARY ${CMAKE_SOURCE_DIR}/machine_translator/Translator/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libTranslator.so)
endif ()


set(FMOD_INC_DIR "${CMAKE_SOURCE_DIR}/third_party/fmod/src/main/include")
set(FMOD_LIB "${CMAKE_SOURCE_DIR}/third_party/fmod/src/main/jniLibs/${ANDROID_ABI}/libfmod.so")

set(libpng_INCLUDE_DIRS "${CMAKE_SOURCE_DIR}/third_party/libpng/src/main/jni/libpng"
        "${CMAKE_SOURCE_DIR}/third_party/libpng/src/main/jni/include")

if (IS_DEBUG_BUILD)
    set(libpng_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/libpng/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libpng16d.so)
else ()
    set(libpng_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/libpng/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libpng16.so)
endif ()

set(SDL3_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/sdl3/include")
if (IS_DEBUG_BUILD)
    set(SDL3_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl3/android-project/app/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL3.so)
else()
    set(SDL3_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl3/android-project/app/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL3.so)
endif ()

set(PSYDOOM_INCLUDE_LAUNCHER FALSE)
set(APP_ROOT_DIR "${CMAKE_SOURCE_DIR}/app/src/main/jni")

add_subdirectory("${APP_ROOT_DIR}/WolfensteinRPG-RE")
add_subdirectory("${APP_ROOT_DIR}/DoomRPG-RE")
add_subdirectory("${APP_ROOT_DIR}/DoomIIRPG-RE")
add_subdirectory("${APP_ROOT_DIR}/Doom64EX-Plus")
add_subdirectory("${APP_ROOT_DIR}/DOOM64-EX-Plus-Enhanced")
#add_subdirectory("${APP_ROOT_DIR}/PsyDoom")

target_include_directories(DoomRPG PRIVATE ${SDL2_INCLUDE_DIR} ${SDL2_MIXER_INCLUDE_DIR}
${SDL2_TTF_INCLUDE_DIR} ${TRANSLATOR_INCLUDE_DIR} ${FLUIDSYNTH_INCLUDE_DIR})

target_link_libraries(DoomRPG PRIVATE ${SDL2_LIBRARY} ${SDL2_MIXER_LIBRARY}
        ${SDL2_TTF_LIBRARY} ZLIB::ZLIB ${TRANSLATOR_LIBRARY} ${FLUIDSYNTH_LIBRARY})

target_include_directories(DoomIIRPG PUBLIC "${OPENAL_INCLUDE_DIR}/AL" ${SDL2_INCLUDE_DIR}
        ${SDL2_TTF_INCLUDE_DIR} ${OPENGL_INCLUDE_DIR} ${TRANSLATOR_INCLUDE_DIR})

target_link_libraries(DoomIIRPG PRIVATE ${SDL2_LIBRARY} ${SDL2_TTF_LIBRARY} ZLIB::ZLIB
        ${OPENGL_opengl_LIBRARY} ${OPENAL_LIBRARY} ${TRANSLATOR_LIBRARY})

target_include_directories(WolfensteinRPG PRIVATE "${OPENAL_INCLUDE_DIR}" ${SDL2_INCLUDE_DIR}
        ${SDL2_TTF_INCLUDE_DIR} ${OPENGL_INCLUDE_DIR} ${TRANSLATOR_INCLUDE_DIR})

target_link_libraries(WolfensteinRPG PRIVATE ${SDL2_LIBRARY} ${SDL2_TTF_LIBRARY} ZLIB::ZLIB
        ${OPENGL_opengl_LIBRARY} ${OPENAL_LIBRARY} ${TRANSLATOR_LIBRARY})

target_include_directories(DOOM64 PRIVATE  ${SDL3_INCLUDE_DIR} ${OPENGL_INCLUDE_DIR} ${libpng_INCLUDE_DIRS}
        ${FMOD_INC_DIR} ${OPENGL_INCLUDE_DIR})

target_link_libraries(DOOM64 PRIVATE ${SDL3_LIBRARY} ${OPENGL_opengl_LIBRARY} ${libpng_LIBRARY}
        ${FMOD_LIB} ZLIB::ZLIB m log )

target_include_directories(DOOM64-Enhanced PRIVATE  ${SDL3_INCLUDE_DIR} ${OPENGL_INCLUDE_DIR} ${libpng_INCLUDE_DIRS}
        ${FMOD_INC_DIR} ${OPENGL_INCLUDE_DIR})

target_link_libraries(DOOM64-Enhanced PRIVATE ${SDL3_LIBRARY} ${OPENGL_opengl_LIBRARY} ${libpng_LIBRARY}
        ${FMOD_LIB} ZLIB::ZLIB m log )

add_library(SDL3MouseHelper SHARED)
target_sources(SDL3MouseHelper PRIVATE "${APP_ROOT_DIR}/SDL3MouseIconHelper.c")
target_include_directories(SDL3MouseHelper PRIVATE ${SDL3_INCLUDE_DIR})
target_link_libraries(SDL3MouseHelper PRIVATE ${SDL3_LIBRARY})