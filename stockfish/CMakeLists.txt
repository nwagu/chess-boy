cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_VERBOSE_MAKEFILE 1)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c11")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -std=c++17")

add_library( # Specifies the name of the library.
        stockfish
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        src/commonMain/cpp/benchmark.cpp
        src/commonMain/cpp/bitbase.cpp
        src/commonMain/cpp/bitboard.cpp
        src/commonMain/cpp/endgame.cpp
        src/commonMain/cpp/evaluate.cpp
        src/commonMain/cpp/stockfish-lib.cpp
        src/commonMain/cpp/material.cpp
        src/commonMain/cpp/misc.cpp
        src/commonMain/cpp/movegen.cpp
        src/commonMain/cpp/movepick.cpp
        src/commonMain/cpp/pawns.cpp
        src/commonMain/cpp/position.cpp
        src/commonMain/cpp/psqt.cpp
        src/commonMain/cpp/search.cpp
        src/commonMain/cpp/thread.cpp
        src/commonMain/cpp/timeman.cpp
        src/commonMain/cpp/tt.cpp
        src/commonMain/cpp/uci.cpp
        src/commonMain/cpp/ucioption.cpp
        src/commonMain/cpp/syzygy/tbprobe.cpp
        )

# Specifies a path to native header files.
include_directories(
        src/commonMain/cpp/
        src/commonMain/cpp/syzygy
)

target_link_libraries(stockfish log)

find_package(SWIG REQUIRED)
include(${SWIG_USE_FILE})
