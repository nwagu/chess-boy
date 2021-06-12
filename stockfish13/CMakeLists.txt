cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_STANDARD 17)


set(SWIG_I_FILE "src/main/cpp/stockfish.i")
set(JAVA_GEN_PACKAGE "com.nwagu.stockfish13")
string(REPLACE "." "/" JAVA_GEN_SUBDIR ${JAVA_GEN_PACKAGE})
set(JAVA_GEN_DIR ${Project_SOURCE_DIR}/src/main/java/${JAVA_GEN_SUBDIR})

set(CMAKE_VERBOSE_MAKEFILE 1) # Use 1 for debugging, 0 for release

# Sets some warning/error conditions and which version of C/C++ to use
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c11")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -std=c++17")


add_library( # Specifies the name of the library.
             stockfish13
             # Sets the library as a shared library.
             SHARED
             # Provides a relative path to your source file(s).
             src/main/cpp/benchmark.cpp
             src/main/cpp/bitbase.cpp
             src/main/cpp/bitboard.cpp
             src/main/cpp/endgame.cpp
             src/main/cpp/evaluate.cpp
             src/main/cpp/main.cpp
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
             src/main/cpp/tune.cpp
             src/main/cpp/uci.cpp
             src/main/cpp/ucioption.cpp
             src/main/cpp/syzygy/tbprobe.cpp
             src/main/cpp/nnue/evaluate_nnue.cpp
             src/main/cpp/nnue/features/half_kp.cpp
        )

# Specifies a path to native header files.
include_directories(
        src/main/cpp/
        src/main/cpp/incbin
        src/main/cpp/syzygy
        src/main/cpp/nnue
        src/main/cpp/nnue/architectures
        src/main/cpp/nnue/features
        src/main/cpp/nnue/layers
)


# Fail the build if SWIG is not found - Ensure SWIG is installed and on the system path
# TODO: Does this run for EACH architecture?
find_package(SWIG REQUIRED)
include(${SWIG_USE_FILE})

# Remove old java files, in case we don't need to generate some of them anymore
file(REMOVE_RECURSE ${JAVA_GEN_DIR})

# Ensure file recognized as C++ (otherwise, exported as C file)
set_property(SOURCE src/main/cpp/stockfish.i PROPERTY CPLUSPLUS ON)

# Setup SWIG flags and locations
set(CMAKE_SWIG_FLAGS -c++ -package ${JAVA_GEN_PACKAGE})
set(CMAKE_SWIG_OUTDIR ${JAVA_GEN_DIR})

# Export a wrapper file to Java, and link with the created C++ library
swig_add_module(stockfish13Wrapper java ${SWIG_I_FILE})
swig_link_libraries(stockfish13Wrapper stockfish13)
