package com.nwagu.chess.representation

import com.nwagu.chess.gamelogic.*
import com.nwagu.chess.model.*

val uciMovePattern = Regex("""([a-h]{1}[1-8]{1})([a-h]{1}[1-8]{1})([qrbn])?""")

fun Board.parseUciEngineMove(move: String): Move? {

    if (!uciMovePattern.matches(move))
        return null

    val source = coordinateToSquare(move.take(2))
    val destination = coordinateToSquare(move.substring(2, 4))

    if (move.length == 4) {

        if (getSquareOccupant(source).chessPieceType == ChessPieceType.PAWN && !areSquaresOnSameColumn(source, destination) && squareEmpty(destination)) {
            return EnPassant(source, destination)
        } else {

            if (getSquareOccupant(source).chessPieceType == ChessPieceType.KING &&
                areSquaresOnSameRow(source, destination) &&
                squaresBetweenSquaresOnRow(
                    source,
                    destination
                ).isNotEmpty()
            ) {
                return Castling(source, destination)
            }

            return RegularMove(source, destination)
        }

    } else if (move.length == 5) {
        val promotionPieceType = chessPieceTypeWithSanSymbol(move.takeLast(1).toUpperCase())
        return Promotion(source, destination, promotionPieceType)
    }

    return null
}