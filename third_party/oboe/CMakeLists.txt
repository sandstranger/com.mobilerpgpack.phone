cmake_minimum_required(VERSION 4.1)
project(oboe_builder)

set(BUILD_SHARED_LIBS ON)
set(OBOE_DISABLE_CONVERSION ON CACHE STRING "" FORCE)
add_definitions(-DOBOE_ENABLE_AAUDIO=0)
add_subdirectory("src/main/jni/oboe")