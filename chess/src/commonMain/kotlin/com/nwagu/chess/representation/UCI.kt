package com.nwagu.chess.representation

import com.nwagu.chess.gamelogic.areSquaresOnSameColumn
import com.nwagu.chess.gamelogic.areSquaresOnSameRow
import com.nwagu.chess.gamelogic.squareEmpty
import com.nwagu.chess.gamelogic.squaresBetweenSquaresOnRow
import com.nwagu.chess.model.Board
import com.nwagu.chess.model.Castling
import com.nwagu.chess.model.ChessPieceType
import com.nwagu.chess.model.EnPassant
import com.nwagu.chess.model.Move
import com.nwagu.chess.model.Promotion
import com.nwagu.chess.model.RegularMove
import com.nwagu.chess.model.chessPieceTypeWithSanSymbol

val uciMovePattern = Regex("""([a-h]{1}[1-8]{1})([a-h]{1}[1-8]{1})([qrbn])?""")

fun Board.parseUciEngineMove(move: String): Move? {

    if (!uciMovePattern.matches(move))
        return null

    val source = coordinateToSquare(move.take(2))
    val destination = coordinateToSquare(move.substring(2, 4))

    if (move.length == 4) {

        if (getSquareOccupantAsChessPiece(source).chessPieceType == ChessPieceType.PAWN && !areSquaresOnSameColumn(source, destination) && squareEmpty(destination)) {
            return EnPassant(source, destination)
        } else {

            if (getSquareOccupantAsChessPiece(source).chessPieceType == ChessPieceType.KING &&
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
        val promotionPieceType = chessPieceTypeWithSanSymbol(move.takeLast(1).uppercase())
        return Promotion(source, destination, promotionPieceType)
    }

    return null
}