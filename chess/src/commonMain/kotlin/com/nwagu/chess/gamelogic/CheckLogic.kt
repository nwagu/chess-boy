package com.nwagu.chess.gamelogic

import com.nwagu.chess.model.*
import com.nwagu.chess.gamelogic.moves.*

fun Board.validateMoveDoesNotLeaveKingExposed(move: Move): Boolean {
    move(move, false)
    val kingNotExposed = !isOnCheck(getSquareOccupant(move.destination).chessPieceColor)
    undoMove()
    return kingNotExposed
}

fun Board.isOnCheck(color: ChessPieceColor) = whoIsCheckingKingColored(color).isNotEmpty()

fun Board.isCheckMate(color: ChessPieceColor = turn): Boolean {
    return isOnCheck(color) &&
            squaresWithPiecesColored(color).all {
                getPossibleMovesFrom(it).isEmpty()
            }
}

fun Board.isDraw(color: ChessPieceColor = turn): Boolean {
    return isStaleMate(color)
}

fun Board.isStaleMate(color: ChessPieceColor = turn): Boolean {
    return squaresWithPiecesColored(color).all {
        getPossibleMovesFrom(it).isEmpty()
    }
}

fun Board.whoIsCheckingKingColored(color: ChessPieceColor): List<Int> {

    val kingPosition = if (color == ChessPieceColor.BLACK) blackKingPosition else whiteKingPosition

    return squaresWithPiecesColored(color.opposite()).filter {
        canPieceMoveFrom(it, kingPosition)
    }
}