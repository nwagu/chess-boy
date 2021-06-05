package com.nwagu.chess.board

import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.opposite

val Board.turn: ChessPieceColor
    get() {
        try {
            val lastMove = movesHistory.lastOrNull() ?: return ChessPieceColor.WHITE
            return getSquareOccupant(lastMove.destination).chessPieceColor.opposite()
        } catch (e: Exception) {
            e.printStackTrace()
            TODO()
        }
    }