package com.nwagu.chess.gamelogic.moves

import com.nwagu.chess.gamelogic.*
import com.nwagu.chess.model.*

fun Board.getQueenMovesFrom(source: Square): List<Move> {

    if (getSquareOccupant(source).chessPieceType != ChessPieceType.QUEEN)
        throw IllegalStateException("Type must be QUEEN!")

    return this.squaresMap.keys.filter { destination ->
        canQueenMoveFrom(source, destination) &&
                destinationIsEmptyOrHasEnemy(destination, getSquareOccupant(source).chessPieceColor)
    }.map { destination ->
        RegularMove(source, destination)
    }

}

fun Board.canQueenMoveFrom(source: Square, destination: Square): Boolean {

    // A queen can go to squares that are on the same row, column or diagonal
    return destination != source &&
            (areSquaresClearOnSameRow(source, destination) ||
                    areSquaresClearOnSameColumn(source, destination) ||
                    areSquaresClearOnSameDiagonal(source, destination))
}