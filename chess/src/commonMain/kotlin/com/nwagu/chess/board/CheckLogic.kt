package com.nwagu.chess.board

import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.enums.opposite
import com.nwagu.chess.moves.*

fun Board.validateMoveDoesNotLeaveKingExposed(move: Move): Boolean {
    move(move)
    val kingNotExposed = !isOnCheck(getCellOccupant(move.destination).chessPieceColor)
    undoMove()
    return kingNotExposed
}

fun Board.isOnCheck(color: ChessPieceColor) = whoIsCheckingKingColored(color).isNotEmpty()

fun Board.isCheckMate(color: ChessPieceColor = turn): Boolean {
    return isOnCheck(color) &&
            positionsWithPiecesColored(color).all {
                getPossibleMovesFrom(it).isEmpty()
            }
}

fun Board.isDraw(color: ChessPieceColor = turn): Boolean {
    return isStaleMate(color)
}

fun Board.isStaleMate(color: ChessPieceColor = turn): Boolean {
    return positionsWithPiecesColored(color).all {
        getPossibleMovesFrom(it).isEmpty()
    }
}

fun Board.whoIsCheckingKingColored(color: ChessPieceColor): List<Int> {

    val kingPosition = if (color == ChessPieceColor.BLACK) blackKingPosition else whiteKingPosition

    return positionsWithPiecesColored(color.opposite()).filter {
        canPieceMoveFrom(it, kingPosition)
    }
}

fun Board.canPieceMoveFrom(source: Int, destination: Int): Boolean {

    if (squaresMap[source] == EmptySquare)
        return false

    return when((squaresMap[source] as ChessPiece).chessPieceType) {
        ChessPieceType.QUEEN -> canQueenMoveFrom(source, destination)
        ChessPieceType.KING -> canKingMoveFrom(source, destination)
        ChessPieceType.KNIGHT -> canKnightMoveFrom(source, destination)
        ChessPieceType.BISHOP -> canBishopMoveFrom(source, destination)
        ChessPieceType.ROOK -> canRookMoveFrom(source, destination)
        ChessPieceType.PAWN -> canPawnMoveFrom(source, destination)
    }
}