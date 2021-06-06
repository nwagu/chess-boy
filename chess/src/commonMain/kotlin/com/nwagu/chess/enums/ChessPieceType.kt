package com.nwagu.chess.enums

enum class ChessPieceType(val sanSymbol: String, val fenSymbol: String) {
    QUEEN("Q", "Q"),
    KING("K", "K"),
    KNIGHT("N", "N"),
    BISHOP("B", "B"),
    ROOK("R", "R"),
    PAWN("", "P")
}

fun pieceSanSymbols(): List<String> = listOf("Q", "K", "N", "B", "R")
fun pieceFenSymbols(): List<String> = listOf("Q", "K", "N", "B", "R", "P")

fun chessPieceTypeWithSanSymbol(sanSymbol: String): ChessPieceType {
    return when(sanSymbol) {
        ChessPieceType.QUEEN.sanSymbol -> ChessPieceType.QUEEN
        ChessPieceType.KING.sanSymbol -> ChessPieceType.KING
        ChessPieceType.KNIGHT.sanSymbol -> ChessPieceType.KNIGHT
        ChessPieceType.BISHOP.sanSymbol -> ChessPieceType.BISHOP
        ChessPieceType.ROOK.sanSymbol -> ChessPieceType.ROOK
        ChessPieceType.PAWN.sanSymbol -> ChessPieceType.PAWN
        else -> throw IllegalStateException("Unknown SAN symbol!")
    }
}

fun chessPieceTypeWithFenSymbol(fenSymbol: String): ChessPieceType {
    return when(fenSymbol.toUpperCase()) {
        ChessPieceType.QUEEN.fenSymbol -> ChessPieceType.QUEEN
        ChessPieceType.KING.fenSymbol -> ChessPieceType.KING
        ChessPieceType.KNIGHT.fenSymbol -> ChessPieceType.KNIGHT
        ChessPieceType.BISHOP.fenSymbol -> ChessPieceType.BISHOP
        ChessPieceType.ROOK.fenSymbol -> ChessPieceType.ROOK
        ChessPieceType.PAWN.fenSymbol -> ChessPieceType.PAWN
        else -> throw IllegalStateException("Unknown FEN symbol!")
    }
}