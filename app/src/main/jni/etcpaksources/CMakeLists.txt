set(ETCPACK_SOURCE_DIR "${CMAKE_CURRENT_SOURCE_DIR}/etcpak")

include_directories(${ETCPACK_SOURCE_DIR})

set(SOURCES "${ETCPACK_SOURCE_DIR}/bc7enc.cpp"
        "${ETCPACK_SOURCE_DIR}/bcdec.c"
        "${ETCPACK_SOURCE_DIR}/BlockData.cpp"
        "${ETCPACK_SOURCE_DIR}/ColorSpace.cpp"
        "${ETCPACK_SOURCE_DIR}/DataProvider.cpp"
        "${ETCPACK_SOURCE_DIR}/Debug.cpp"
        "${ETCPACK_SOURCE_DIR}/Decode.cpp"
        "${ETCPACK_SOURCE_DIR}/Dither.cpp"
        "${ETCPACK_SOURCE_DIR}/Error.cpp"
        "${ETCPACK_SOURCE_DIR}/mmap.cpp"
        "${ETCPACK_SOURCE_DIR}/ProcessDxtc.cpp"
        "${ETCPACK_SOURCE_DIR}/ProcessRGB.cpp"
        "${ETCPACK_SOURCE_DIR}/System.cpp"
        "${ETCPACK_SOURCE_DIR}/Tables.cpp"
        "${ETCPACK_SOURCE_DIR}/TaskDispatch.cpp"
        "${ETCPACK_SOURCE_DIR}/TextureHeader.cpp"
        "${ETCPACK_SOURCE_DIR}/Timing.cpp")

add_library(etcpak STATIC ${SOURCES})
