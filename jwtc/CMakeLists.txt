cmake_minimum_required(VERSION 3.4.1)

add_library( # Specifies the name of the library.
             jwtc
             # Sets the library as a shared library.
             SHARED
             # Provides a relative path to your source file(s).
             src/main/cpp/chess-jni.cpp
             src/main/cpp/Pos.cpp
             src/main/cpp/Move.cpp
             src/main/cpp/ChessBoard.cpp
             src/main/cpp/Game.cpp)

# Specifies a path to native header files.
include_directories(src/main/cpp/)