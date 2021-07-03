package com.nwagu.chess.gamelogic.moves

import com.nwagu.chess.gamelogic.squareEmpty
import com.nwagu.chess.gamelogic.validateMoveDoesNotLeaveKingExposed
import com.nwagu.chess.model.*

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

fun Board.getPossibleMovesFrom(source: Square): List<Move> {

    if (squareEmpty(source))
        return emptyList()

    val possibleMoves = when (getSquareOccupant(source).chessPieceType) {
        ChessPieceType.QUEEN -> {
            getQueenMovesFrom(source)
        }
        ChessPieceType.KING -> {
            getKingMovesFrom(source)
        }
        ChessPieceType.KNIGHT -> {
            getKnightMovesFrom(source)
        }
        ChessPieceType.BISHOP -> {
            getBishopMovesFrom(source)
        }
        ChessPieceType.ROOK -> {
            getRookMovesFrom(source)
        }
        ChessPieceType.PAWN -> {
            getPawnMovesFrom(source)
        }
    }

    return possibleMoves.filter {
        // a move cannot leave a king exposed
        validateMoveDoesNotLeaveKingExposed(it)
    }.filter {
        // kings can not be captured
        it.destination !in listOf(whiteKingPosition, blackKingPosition)
    }
}