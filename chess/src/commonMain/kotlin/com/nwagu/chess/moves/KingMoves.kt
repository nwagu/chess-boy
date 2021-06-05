package com.nwagu.chess.moves

import com.nwagu.chess.board.*
import com.nwagu.chess.enums.ChessPieceType
import kotlin.math.abs

fun Board.getKingMovesFrom(source: Square): List<Move> {

    if (getSquareOccupant(source).chessPieceType != ChessPieceType.KING)
        throw IllegalStateException("Type must be KING!")

    return this.squaresMap.keys.filter { destination ->
        canKingMoveFrom(source, destination) &&
                destinationIsEmptyOrHasEnemy(destination, getSquareOccupant(source).chessPieceColor)
    }.map { destination ->
        if (abs(column(source) - column(destination)) == 2)
            Castling(source, destination, getCastlePartnerSourceForKingMove(source, destination), getCastlePartnerDestinationForKingMove(source, destination))
        else
            RegularMove(source, destination)
    }
}

fun Board.canKingMoveFrom(source: Square, destination: Square): Boolean {

    val king = squaresMap[source] as ChessPiece

    return destination != source &&
            (abs(row(destination) - row(source)) <= 1 && abs(column(destination) - column(source)) <= 1)

            ||

            // castling
            kotlin.run {

                val kingColumn = column(source)
                val destColumn = column(destination)

                (areSquaresClearOnSameRow(source, destination) && abs(destColumn - kingColumn) == 2 && king.numberOfMovesMade == 0 &&
                        squaresMap[getCastlePartnerSourceForKingMove(source, destination)].let { it is ChessPiece && it.numberOfMovesMade == 0 && it.chessPieceType == ChessPieceType.ROOK } &&
                        areSquaresClearOnSameRow(source, getCastlePartnerSourceForKingMove(source, destination))) &&
                        !isOnCheck(king.chessPieceColor)
            }

}

fun Board.getCastlePartnerSourceForKingMove(source: Square, destination: Square): Square {
    return square(row(source), if (column(source) > column(destination)) 0 else numberOfColumns - 1)
}

fun Board.getCastlePartnerDestinationForKingMove(source: Square, destination: Square): Square {
    return if (column(source) > column(destination)) source - 1 else source + 1
}