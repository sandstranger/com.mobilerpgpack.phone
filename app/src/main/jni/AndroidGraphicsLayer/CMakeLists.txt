cmake_minimum_required(VERSION 4.1)
project(AndroidGraphicsLayer)

set(CMAKE_C_COMPILER_LAUNCHER ccache)
set(CMAKE_CXX_COMPILER_LAUNCHER ccache)

set(CMAKE_C_STANDARD 23)
set(CMAKE_C_STANDARD_REQUIRED ON)

set (CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS ON)

include_directories("${CMAKE_CURRENT_SOURCE_DIR}/include")
add_library(AndroidGraphicsLayer SHARED)
target_sources(AndroidGraphicsLayer PRIVATE "${CMAKE_CURRENT_SOURCE_DIR}/SDL3Helper.c"
        "${CMAKE_CURRENT_SOURCE_DIR}/SDL3GyroInput.c"
        "${CMAKE_CURRENT_SOURCE_DIR}/SDL3VirtualController.c"
        "${CMAKE_CURRENT_SOURCE_DIR}/AngleShaderCache.cpp"
        "${CMAKE_CURRENT_SOURCE_DIR}/SwappyController.cpp")
target_include_directories(AndroidGraphicsLayer PRIVATE ${SDL3_INCLUDE_DIR})
target_link_libraries(AndroidGraphicsLayer PRIVATE ${SDL3_LIBRARY} android log games-frame-pacing::swappy_static)