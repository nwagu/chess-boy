package com.nwagu.chess.moves

import com.nwagu.chess.board.Board
import com.nwagu.chess.board.Square
import com.nwagu.chess.board.squareEmpty
import com.nwagu.chess.board.validateMoveDoesNotLeaveKingExposed
import com.nwagu.chess.enums.ChessPieceType

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