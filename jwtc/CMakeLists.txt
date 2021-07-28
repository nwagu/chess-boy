cmake_minimum_required(VERSION 3.4.1)

add_library( # Specifies the name of the library.
             jwtc
             # Sets the library as a shared library.
             SHARED
             # Provides a relative path to your source file(s).
             src/androidMain/cpp/chess-jni.cpp
             src/commonMain/cpp/Pos.cpp
             src/commonMain/cpp/Move.cpp
             src/commonMain/cpp/ChessBoard.cpp
             src/commonMain/cpp/Game.cpp)

# Specifies a path to native header files.
include_directories(src/commonMain/cpp/)