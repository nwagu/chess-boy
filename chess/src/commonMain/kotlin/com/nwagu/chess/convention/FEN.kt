package com.nwagu.chess.convention

import com.nwagu.chess.board.*
import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.enums.chessPieceTypeWithFenSymbol

const val STARTING_POSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

fun Board.loadStandardStartingPosition() {

    if (numberOfColumns != 8 && numberOfRows != 8)
        throw IllegalStateException("This method requires an 8x8 board!")

    loadPositionFromFen(STARTING_POSITION_FEN)

    blackKingPosition = 4
    whiteKingPosition = 60

}

/*
* This method loads only the chess piece positions on the board.
* It does not take into account other information in the FEN input
* */
fun Board.loadPositionFromFen(fen: String) {

    val pieceArrangement = fen.takeWhile { it != ' ' }

    val rowPositions = pieceArrangement.split("/")
    var column: Int
    for ((row, positions) in rowPositions.withIndex()) {
        column = 0
        for (element in positions) {

            val square = square(row, column)

            val fenSymbol = element.toString()

            if (fenSymbol.toIntOrNull() != null) {
                // numeric character represents consecutive number of empty squares
                setSquareOccupant(square, EmptySquare)
                column += fenSymbol.toInt()
            } else {
                val piece = ChessPiece(
                    chessPieceTypeWithFenSymbol(fenSymbol),
                    if (element.isUpperCase()) ChessPieceColor.WHITE else ChessPieceColor.BLACK,
                    0,
                    row, column
                )
                setSquareOccupant(square, piece)
                column++
            }
        }
    }

}

fun Board.getFen(): String {

    val fen = StringBuilder()

    var emptySquaresCount = 0

    // board positions
    repeat(squaresMap.size) { square ->
        if (square % numberOfColumns == 0 && square != 0)
            fen.append("/")

        if (squareEmpty(square)) {
            emptySquaresCount++
            if ((square + 1) % numberOfColumns == 0) {
                fen.append(emptySquaresCount)
                emptySquaresCount = 0
            }
            return@repeat
        }

        val piece = getSquareOccupant(square)

        if (emptySquaresCount > 0) {
            fen.append(emptySquaresCount)
            emptySquaresCount = 0
        }

        var fenSymbol = piece.chessPieceType.fenSymbol
        if (piece.chessPieceColor == ChessPieceColor.BLACK)
            fenSymbol = fenSymbol.toLowerCase()

        fen.append(fenSymbol)
    }

    // next to play
    fen.append(if (turn == ChessPieceColor.WHITE) " w" else " b")

    // castling availability
    fen.append(" ${getCastlingAvailabilityInFen()}")

    // en passant square
    fen.append(" ${getPossibleEnPassantTargetSquareInFen()}")

    // moves count
    fen.append(" $halfMoveCountSinceLastCaptureOrPawnAdvance $fullMovesNumber")

    return fen.toString()

}

fun Board.getCastlingAvailabilityInFen(): String {
    var availability = ""

    if(isWhiteKingSideCastlingAvailable) availability = "${availability}K"
    if(isWhiteQueenSideCastlingAvailable) availability = "${availability}Q"
    if(isBlackKingSideCastlingAvailable) availability = "${availability}k"
    if(isBlackQueenSideCastlingAvailable) availability = "${availability}q"

    return if (availability.isEmpty()) "-" else availability

}

fun Board.getPossibleEnPassantTargetSquareInFen(): String {
    val lastMove = movesHistory.lastOrNull() ?: return "-"

    if (!lastMove.isByType(ChessPieceType.PAWN))
        return "-"

    try {
        squaresBetweenSquaresOnColumn(lastMove.source, lastMove.destination).let {
            if (it.size != 1)
                return "-"
            else
                return squareToCoordinate(it[0])
        }
    } catch (e: IllegalArgumentException) {
        // last move was a pawn capture
        return "-"
    }

}
