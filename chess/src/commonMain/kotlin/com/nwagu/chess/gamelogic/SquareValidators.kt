package com.nwagu.chess.gamelogic

import com.nwagu.chess.model.*

fun Board.squareEmpty(square: Square) = squaresMap[square] is EmptySquare

fun Board.squareContainsChessPieceColored(square: Square, color: ChessPieceColor): Boolean {
    return getSquareOccupantAsChessPieceOrNull(square)?.chessPieceColor == color
}

fun Board.squareOccupantHasNotMoved(square: Square): Boolean {
    if (squareEmpty(square))
        return false

    return getSquareOccupantAsChessPiece(square).numberOfMovesMade == 0
}

fun Board.destinationIsEmptyOrHasEnemy(destination: Square, color: ChessPieceColor): Boolean {
    return if (squareEmpty(destination))
        true
    else
        getSquareOccupantAsChessPiece(destination).chessPieceColor != color
}

fun Board.destinationContainsAnEnemy(destination: Square, color: ChessPieceColor): Boolean {
    squaresMap[destination].let {
        return it is ChessPiece && it.chessPieceColor != color
    }
}

fun Board.isPawnPromotionSquare(square: Square): Boolean {
    return row(square) == 0 || row(square) == numberOfRows - 1
}