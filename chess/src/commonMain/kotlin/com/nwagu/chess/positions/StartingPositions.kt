package com.nwagu.chess.positions

import com.nwagu.chess.board.*
import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.ChessPieceType

fun Board.populateCellsWithEmpties() {
    repeat(numberOfColumns * numberOfRows) { index ->
        squaresMap[index] = EmptySquare
    }
}

fun Board.setToStandardStartingPosition() {

    if (numberOfColumns != 8 && numberOfRows != 8)
        throw IllegalStateException("Standard game arrangement requires an 8x8 board!")

    for (index in listOf(0,7)) {
        squaresMap[index] = ChessPiece(
            chessPieceType = ChessPieceType.ROOK,
            chessPieceColor = ChessPieceColor.BLACK,
            numberOfMovesMade = 0,
            startingColumn = column(index),
            startingRow = row(index)
        )
    }

    for (index in listOf(1,6)) {
        squaresMap[index] = ChessPiece(
            chessPieceType = ChessPieceType.KNIGHT,
            chessPieceColor = ChessPieceColor.BLACK,
            numberOfMovesMade = 0,
            startingColumn = column(index),
            startingRow = row(index)
        )
    }

    for (index in listOf(2,5)) {
        squaresMap[index] = ChessPiece(
            chessPieceType = ChessPieceType.BISHOP,
            chessPieceColor = ChessPieceColor.BLACK,
            numberOfMovesMade = 0,
            startingColumn = column(index),
            startingRow = row(index)
        )
    }

    squaresMap[3] = ChessPiece(
        chessPieceType = ChessPieceType.QUEEN,
        chessPieceColor = ChessPieceColor.BLACK,
        numberOfMovesMade = 0,
        startingColumn = column(3),
        startingRow = row(3)
    )

    squaresMap[4] = ChessPiece(
        chessPieceType = ChessPieceType.KING,
        chessPieceColor = ChessPieceColor.BLACK,
        numberOfMovesMade = 0,
        startingColumn = column(4),
        startingRow = row(4)
    )

    blackKingPosition = 4

    for (index in 8..15) {
        squaresMap[index] = ChessPiece(
            chessPieceType = ChessPieceType.PAWN,
            chessPieceColor = ChessPieceColor.BLACK,
            numberOfMovesMade = 0,
            startingColumn = column(index),
            startingRow = row(index)
        )
    }


    for (index in 16..47) {
        squaresMap[index] = EmptySquare
    }

    for (index in 48..55) {
        squaresMap[index] = ChessPiece(
            chessPieceType = ChessPieceType.PAWN,
            chessPieceColor = ChessPieceColor.WHITE,
            numberOfMovesMade = 0,
            startingColumn = column(index),
            startingRow = row(index)
        )
    }

    for (index in listOf(56,63)) {
        squaresMap[index] = ChessPiece(
            chessPieceType = ChessPieceType.ROOK,
            chessPieceColor = ChessPieceColor.WHITE,
            numberOfMovesMade = 0,
            startingColumn = column(index),
            startingRow = row(index)
        )
    }

    for (index in listOf(57,62)) {
        squaresMap[index] = ChessPiece(
            chessPieceType = ChessPieceType.KNIGHT,
            chessPieceColor = ChessPieceColor.WHITE,
            numberOfMovesMade = 0,
            startingColumn = column(index),
            startingRow = row(index)
        )
    }

    for (index in listOf(58,61)) {
        squaresMap[index] = ChessPiece(
            chessPieceType = ChessPieceType.BISHOP,
            chessPieceColor = ChessPieceColor.WHITE,
            numberOfMovesMade = 0,
            startingColumn = column(index),
            startingRow = row(index)
        )
    }

    squaresMap[59] = ChessPiece(
        chessPieceType = ChessPieceType.QUEEN,
        chessPieceColor = ChessPieceColor.WHITE,
        numberOfMovesMade = 0,
        startingColumn = column(59),
        startingRow = row(59)
    )

    squaresMap[60] = ChessPiece(
        chessPieceType = ChessPieceType.KING,
        chessPieceColor = ChessPieceColor.WHITE,
        numberOfMovesMade = 0,
        startingColumn = column(60),
        startingRow = row(60)
    )

    whiteKingPosition = 60

}