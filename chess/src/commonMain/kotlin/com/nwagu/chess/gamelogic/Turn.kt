package com.nwagu.chess.gamelogic

import com.nwagu.chess.model.Board
import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chess.model.opposite

val Board.turn: ChessPieceColor
    get() {
        try {
            val lastMove = movesHistory.lastOrNull() ?: return ChessPieceColor.WHITE
            return getSquareOccupantAsChessPiece(lastMove.destination).chessPieceColor.opposite()
        } catch (e: Exception) {
            e.printStackTrace()
            TODO()
        }
    }