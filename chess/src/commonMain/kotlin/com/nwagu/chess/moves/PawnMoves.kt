package com.nwagu.chess.moves

import com.nwagu.chess.board.*
import com.nwagu.chess.enums.ChessPieceType
import kotlin.math.abs

fun Board.getPawnMovesFrom(source: Square): List<Move> {

    try {

        if (getCellOccupant(source).chessPieceType != ChessPieceType.PAWN)
            throw IllegalStateException("Type must be PAWN!")

        return this.squaresMap.keys.filter { destination ->
            canPawnMoveFrom(source, destination) &&
                    destinationIsEmptyOrHasEnemy(
                        destination,
                        getCellOccupant(source).chessPieceColor
                    )
        }.map { destination ->
            if (column(destination) != column(source) && squareEmpty(destination))
                EnPassant(
                    source,
                    destination,
                    captivePosition = square(row(source), column(destination))
                )
            else if (isPawnPromotionSquare(destination))
                Promotion(source, destination, promotionType = ChessPieceType.QUEEN)
            else
                RegularMove(source, destination)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return emptyList()
    }
}

fun Board.canPawnMoveFrom(source: Square, destination: Square): Boolean {

    val pawn = squaresMap[source] as ChessPiece

    if (!(((pawn.startingRow < numberOfRows / 2) && (row(destination) > row(source))) ||
        ((pawn.startingRow >= numberOfRows / 2) && (row(destination) < row(source)))))
        return false

    // The pawn wants to step forward two steps if not moved before
    return (abs(row(destination) - row(source)) == 2 && column(destination) == column(source) && row(source) == pawn.startingRow && squareEmpty(destination))
                ||

                // The pawn wants to step forward one step
                (abs(row(destination) - row(source)) == 1 && column(destination) == column(source) && squareEmpty(destination))

                ||

                // The pawn wants to capture another piece
                (abs(row(destination) - row(source)) == 1 && abs(column(destination) - column(source)) == 1 && destinationContainsAnEnemy(destination, pawn.chessPieceColor))

                ||

                // The pawn wants to capture another piece 'en passant'
                (abs(row(destination) - row(source)) == 1 && abs(column(destination) - column(source)) == 1 && squareEmpty(destination) &&
                        // TODO optimize
                        run {
                            val enPassantVictimPosition = square(row(source), column(destination))

                            // piece captured in en passant move must be a pawn of opposite color
                            squaresMap[enPassantVictimPosition].let {
                                if (it is EmptySquare)
                                    return@run false

                                if (it is ChessPiece) {
                                    if (it.chessPieceType != ChessPieceType.PAWN)
                                        return@run false

                                    if (it.chessPieceColor == pawn.chessPieceColor)
                                        return@run false
                                }
                            }
                            // pawn captured in en passant move must have made the last move
                            if (movesHistory.lastOrNull()?.destination != enPassantVictimPosition)
                                return@run false

                            // pawn captured in en passant move must have done the double move
                            if (abs(row(movesHistory.lastOrNull()?.source!!) - row(source)) != 2)
                                return@run false

                            return true
                        })
}