package com.nwagu.chess.model

sealed class SquareOccupant

object EmptySquare : SquareOccupant()

data class ChessPiece(
    val chessPieceType: ChessPieceType,
    val chessPieceColor: ChessPieceColor,
    var numberOfMovesMade: Int,
    val startingRow: Int,
    val startingColumn: Int
): SquareOccupant()

val ChessPiece.id: String
    get() = "${chessPieceType.fenSymbol}_${startingRow}_${startingColumn}"