package com.nwagu.chess.moves

import com.nwagu.chess.board.*
import com.nwagu.chess.enums.ChessPieceType
import kotlin.math.abs

fun Board.getKnightMovesFrom(source: Square): List<Move> {

    if (getCellOccupant(source).chessPieceType != ChessPieceType.KNIGHT)
        throw IllegalStateException("Type must be KNIGHT!")

    return this.squaresMap.keys.filter { destination ->
        canKnightMoveFrom(source, destination) &&
                destinationIsEmptyOrHasEnemy(destination, getCellOccupant(source).chessPieceColor)
    }.map { destination ->
        RegularMove(source, destination)
    }
}

fun Board.canKnightMoveFrom(source: Square, destination: Square): Boolean {

    // A knight can gaLLop :)
    return (abs(row(destination) - row(source)) == 2 && abs(column(destination) - column(source)) == 1 ||
            abs(row(destination) - row(source)) == 1 && abs(column(destination) - column(source)) == 2)
}