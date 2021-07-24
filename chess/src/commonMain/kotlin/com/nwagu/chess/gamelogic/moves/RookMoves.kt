package com.nwagu.chess.gamelogic.moves

import com.nwagu.chess.gamelogic.*
import com.nwagu.chess.model.*

fun Board.getRookMovesFrom(source: Square): List<Move> {

    if (getSquareOccupantAsChessPiece(source).chessPieceType != ChessPieceType.ROOK)
        throw IllegalStateException("Type must be ROOK!")

    return this.squaresMap.keys.filter { destination ->
        canRookMoveFrom(source, destination) &&
                destinationIsEmptyOrHasEnemy(destination, getSquareOccupantAsChessPiece(source).chessPieceColor)
    }.map { destination ->
        RegularMove(source, destination)
    }
}

fun Board.canRookMoveFrom(source: Square, destination: Square): Boolean {

    // A rook can go to squares that are on the same row or column
    return destination != source &&
            (areSquaresClearOnSameRow(destination, source) || areSquaresClearOnSameColumn(destination, source))
}