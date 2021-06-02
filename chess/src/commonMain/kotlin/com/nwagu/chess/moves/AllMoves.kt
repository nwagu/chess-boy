package com.nwagu.chess.moves

import com.nwagu.chess.board.Board
import com.nwagu.chess.board.EmptySquare
import com.nwagu.chess.board.Square
import com.nwagu.chess.board.validateMoveDoesNotLeaveKingExposed
import com.nwagu.chess.enums.ChessPieceType

fun Board.getPossibleMovesFrom(source: Square): List<Move> {

    if (squaresMap[source] is EmptySquare)
        return emptyList()

    val possibleMoves = when (getCellOccupant(source).chessPieceType) {
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
    }.filter {
        // Must be the mover's turn
        getCellOccupant(it.source).chessPieceColor == turn
    }
}