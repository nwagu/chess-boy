package com.nwagu.chess.gamelogic.moves

import com.nwagu.chess.gamelogic.*
import com.nwagu.chess.model.*
import kotlin.math.abs

fun Board.getKnightMovesFrom(source: Square): List<Move> {

    if (getSquareOccupantAsChessPiece(source).chessPieceType != ChessPieceType.KNIGHT)
        throw IllegalStateException("Type must be KNIGHT!")

    return this.squaresMap.keys.filter { destination ->
        canKnightMoveFrom(source, destination) &&
                destinationIsEmptyOrHasEnemy(destination, getSquareOccupantAsChessPiece(source).chessPieceColor)
    }.map { destination ->
        RegularMove(source, destination)
    }
}

fun Board.canKnightMoveFrom(source: Square, destination: Square): Boolean {

    // A knight can gaLLop :)
    return (abs(row(destination) - row(source)) == 2 && abs(column(destination) - column(source)) == 1 ||
            abs(row(destination) - row(source)) == 1 && abs(column(destination) - column(source)) == 2)
}