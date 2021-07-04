package com.nwagu.chess.gamelogic.moves

import com.nwagu.chess.gamelogic.areSquaresClearOnSameDiagonal
import com.nwagu.chess.gamelogic.destinationIsEmptyOrHasEnemy
import com.nwagu.chess.model.*

fun Board.getBishopMovesFrom(source: Square): List<Move> {

    if (getSquareOccupant(source).chessPieceType != ChessPieceType.BISHOP)
        throw IllegalStateException("Type must be BISHOP!")

    return this.squaresMap.keys.filter { destination ->
        canBishopMoveFrom(source, destination) &&
                destinationIsEmptyOrHasEnemy(destination, getSquareOccupant(source).chessPieceColor)
    }.map { destination ->
        RegularMove(source, destination)
    }
}

fun Board.canBishopMoveFrom(source: Square, destination: Square): Boolean {

    // A bishop can go to squares that are on the same diagonal
    return destination != source && areSquaresClearOnSameDiagonal(destination, source)
}