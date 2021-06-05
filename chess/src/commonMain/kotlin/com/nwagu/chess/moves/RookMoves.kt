package com.nwagu.chess.moves

import com.nwagu.chess.board.*
import com.nwagu.chess.enums.ChessPieceType

fun Board.getRookMovesFrom(source: Square): List<Move> {

    if (getSquareOccupant(source).chessPieceType != ChessPieceType.ROOK)
        throw IllegalStateException("Type must be ROOK!")

    return this.squaresMap.keys.filter { destination ->
        canRookMoveFrom(source, destination) &&
                destinationIsEmptyOrHasEnemy(destination, getSquareOccupant(source).chessPieceColor)
    }.map { destination ->
        RegularMove(source, destination)
    }
}

fun Board.canRookMoveFrom(source: Square, destination: Square): Boolean {

    // A rook can go to cells that are on the same row or column
    return destination != source &&
            (areSquaresClearOnSameRow(destination, source) || areSquaresClearOnSameColumn(destination, source))
}