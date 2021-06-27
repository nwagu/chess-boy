package com.nwagu.chess.moves

import com.nwagu.chess.board.Board
import com.nwagu.chess.board.Square
import com.nwagu.chess.board.areSquaresClearOnSameDiagonal
import com.nwagu.chess.board.destinationIsEmptyOrHasEnemy
import com.nwagu.chess.enums.ChessPieceType

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