package com.nwagu.chess.enums

enum class ChessPieceColor {
    WHITE, BLACK
}

fun ChessPieceColor.opposite(): ChessPieceColor {
    return if (this == ChessPieceColor.WHITE)
        ChessPieceColor.BLACK
    else ChessPieceColor.WHITE
}