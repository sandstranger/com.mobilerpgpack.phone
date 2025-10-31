cmake_minimum_required(VERSION 4.1)
project(Engines_Hub)

find_package(ZLIB REQUIRED)

set(SDL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/sdl2)
set(SDL2_INCLUDE_DIR ${SDL_LOCATION}/include)

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(SDL2_LIBRARY ${SDL_LOCATION}/android-project/app/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2.so)
else()
    set(SDL2_LIBRARY ${SDL_LOCATION}/android-project/app/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2.so)
endif ()

set(OPENAL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/openal)
set(OPENAL_INCLUDE_DIR ${OPENAL_LOCATION}/src/main/jni/openal/include)

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(OPENAL_LIBRARY ${OPENAL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libopenal.so)
else ()
    set(OPENAL_LIBRARY ${OPENAL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libopenal.so)
endif ()

if (NOT DEFINED LEGACY_GLES2 OR LEGACY_GLES2 STREQUAL "false")
    set(OPENGL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/NG-GL4ES)
    set(OPENGL_INCLUDE_DIR ${OPENGL_LOCATION}/src/main/jni/NG-GL4ES/include)
    include_directories(PUBLIC "${OPENGL_INCLUDE_DIR}")
    if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
        set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libng_gl4es.so)
        set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libng_gl4es.so)
    else ()
        set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libng_gl4es.so)
        set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libng_gl4es.so)
    endif ()
else ()
    set(OPENGL_LOCATION ${CMAKE_SOURCE_DIR}/third_party/legacy_gl4es)
    set(OPENGL_INCLUDE_DIR ${OPENGL_LOCATION}/src/main/jni/gl4es/include)
    include_directories(PUBLIC "${OPENGL_INCLUDE_DIR}")
    if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
        set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libgl4es.so)
        set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libgl4es.so)
    else ()
        set(OPENGL_opengl_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libgl4es.so)
        set(OPENGL_glx_LIBRARY ${OPENGL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libgl4es.so)
    endif ()
endif ()

set(SDL2_MIXER_DIR ${CMAKE_SOURCE_DIR}/third_party/sdl2-mixer/src/main/jni/SDL_mixer)
set(SDL2_MIXER_INCLUDE_DIR ${SDL2_MIXER_DIR}/include)
if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(SDL2_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-mixer/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_mixer.so)
else ()
    set(SDL2_MIXER_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-mixer/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_mixer.so)
endif ()

set(FLUIDSYNTH_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/fluidsynth/src/main/jni/fluidsynth-android/include")

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(FLUIDSYNTH_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/fluidsynth/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libfluidsynth.so)
else ()
    set(FLUIDSYNTH_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/fluidsynth/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libfluidsynth.so)
endif ()

set(SDL2_TTF_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/sdl2-ttf/src/main/jni/sdl-ttf")

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(SDL2_TTF_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-ttf/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_ttf.so)
else ()
    set(SDL2_TTF_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl2-ttf/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2_ttf.so)
endif ()

set(TRANSLATOR_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/machine_translator/Translator/src/main/jni/include")

if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(TRANSLATOR_LIBRARY ${CMAKE_SOURCE_DIR}/machine_translator/Translator/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libTranslator.so)
else ()
    set(TRANSLATOR_LIBRARY ${CMAKE_SOURCE_DIR}/machine_translator/Translator/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libTranslator.so)
endif ()


set(FMOD_INC_DIR "${CMAKE_SOURCE_DIR}/third_party/fmod/src/main/include")
set(FMOD_LIB "${CMAKE_SOURCE_DIR}/third_party/fmod/src/main/jniLibs/${ANDROID_ABI}/libfmod.so")

set(libpng_INCLUDE_DIRS "${CMAKE_SOURCE_DIR}/third_party/libpng/src/main/jni/libpng")
set(libpng_CONF_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/app/src/main/jni/include")
if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(libpng_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/libpng/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libpng18d.so)
else ()
    set(libpng_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/libpng/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libpng18.so)
endif ()

set(SDL3_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/third_party/sdl3/include")
if (DEFINED CMAKE_BUILD_TYPE AND CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(SDL3_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl3/android-project/app/build/intermediates/merged_native_libs/debug/mergeDebugNativeLibs/out/lib/${ANDROID_ABI}/libSDL3.so)
else()
    set(SDL3_LIBRARY ${CMAKE_SOURCE_DIR}/third_party/sdl3/android-project/app/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL3.so)
endif ()

add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/WolfensteinRPG-RE")
add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/DoomRPG-RE")
add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/DoomIIRPG-RE")
add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/Doom64EX-Plus")

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
    ${libpng_CONF_INCLUDE_DIR} ${FMOD_INC_DIR} ${OPENGL_INCLUDE_DIR})

target_link_libraries(DOOM64 PRIVATE ${SDL3_LIBRARY} ${OPENGL_opengl_LIBRARY} ${libpng_LIBRARY}
        ${FMOD_LIB} ZLIB::ZLIB m log )