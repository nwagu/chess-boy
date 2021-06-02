package com.nwagu.chess.board

import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.ChessPieceType

sealed class SquareOccupant

object EmptySquare : SquareOccupant()

data class ChessPiece(
    val chessPieceType: ChessPieceType,
    val chessPieceColor: ChessPieceColor,
    var numberOfMovesMade: Int,
    val startingRow: Int,
    val startingColumn: Int
): SquareOccupant()