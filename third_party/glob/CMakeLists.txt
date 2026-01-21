cmake_minimum_required(VERSION 4.1)
project(glob)

set(CMAKE_C_STANDARD 23)
set(CMAKE_C_STANDARD_REQUIRED ON)
set(CMAKE_C_EXTENSIONS ON)

add_library(glob SHARED)
target_include_directories(glob PRIVATE /src/main/jni/glob)
target_sources(glob PRIVATE src/main/jni/glob/glob.c)