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
        src/main/cpp/benchmark.cpp
        src/main/cpp/bitbase.cpp
        src/main/cpp/bitboard.cpp
        src/main/cpp/endgame.cpp
        src/main/cpp/evaluate.cpp
        src/main/cpp/stockfish-lib.cpp
        src/main/cpp/material.cpp
        src/main/cpp/misc.cpp
        src/main/cpp/movegen.cpp
        src/main/cpp/movepick.cpp
        src/main/cpp/pawns.cpp
        src/main/cpp/position.cpp
        src/main/cpp/psqt.cpp
        src/main/cpp/search.cpp
        src/main/cpp/thread.cpp
        src/main/cpp/timeman.cpp
        src/main/cpp/tt.cpp
        src/main/cpp/uci.cpp
        src/main/cpp/ucioption.cpp
        src/main/cpp/syzygy/tbprobe.cpp
        )

# Specifies a path to native header files.
include_directories(
        src/main/cpp/
        src/main/cpp/syzygy
)

target_link_libraries(stockfish log)

find_package(SWIG REQUIRED)
include(${SWIG_USE_FILE})
