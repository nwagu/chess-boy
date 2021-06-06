package com.nwagu.chess.convention

import com.nwagu.chess.board.Board
import com.nwagu.chess.board.areSquaresOnSameColumn
import com.nwagu.chess.board.squareEmpty
import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.enums.chessPieceTypeWithSanSymbol
import com.nwagu.chess.moves.EnPassant
import com.nwagu.chess.moves.Move
import com.nwagu.chess.moves.Promotion
import com.nwagu.chess.moves.RegularMove
import java.util.regex.Pattern

val uciMovePattern = Pattern.compile("([a-h]{1}[1-8]{1})([a-h]{1}[1-8]{1})([qrbn])?")

fun Board.convertChessEngineMoveToMove(move: String): Move? {

    if (!uciMovePattern.matcher(move).matches())
        return null

    val source = coordinateToSquare(move.take(2))
    val destination = coordinateToSquare(move.substring(2, 3))

    if (move.length == 4) {

        // TODO How do you determine a castling move from a chess engine?

        if (getSquareOccupant(source).chessPieceType == ChessPieceType.PAWN && !areSquaresOnSameColumn(source, destination) && squareEmpty(destination)) {
            return EnPassant(source, destination)
        } else {
            RegularMove(source, destination)
        }

    } else if (move.length == 5) {
        val promotionPieceType = chessPieceTypeWithSanSymbol(move.takeLast(1).toUpperCase())
        return Promotion(source, destination, promotionPieceType)
    }

    return null
}