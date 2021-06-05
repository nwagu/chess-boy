package com.nwagu.chess.enums

enum class ChessPieceType(val sanSymbol: String) {
    QUEEN("Q"),
    KING("K"),
    KNIGHT("N"),
    BISHOP("B"),
    ROOK("R"),
    PAWN("")
}

fun pieceSanSymbols(): List<String> = listOf("Q", "K", "N", "B", "R")

fun chessPieceTypeWithSanSymbol(sanSymbol: String): ChessPieceType {
    return when(sanSymbol) {
        "Q" -> ChessPieceType.QUEEN
        "K" -> ChessPieceType.KING
        "N" -> ChessPieceType.KNIGHT
        "B" -> ChessPieceType.BISHOP
        "R" -> ChessPieceType.ROOK
        "" -> ChessPieceType.PAWN
        else -> throw IllegalStateException("Unknown SAN symbol!")
    }
}