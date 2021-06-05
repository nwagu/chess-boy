package com.nwagu.chess.board

import com.nwagu.chess.enums.ChessPieceColor

fun Board.squareEmpty(square: Square) = squaresMap[square] is EmptySquare

fun Board.destinationIsEmptyOrHasEnemy(destination: Square, color: ChessPieceColor): Boolean {
    return if (squareEmpty(destination))
        true
    else
        getSquareOccupant(destination).chessPieceColor != color
}

fun Board.destinationContainsAnEnemy(destination: Square, color: ChessPieceColor): Boolean {
    squaresMap[destination].let {
        return it is ChessPiece && it.chessPieceColor != color
    }
}

fun Board.isPawnPromotionSquare(square: Square): Boolean {
    return row(square) == 0 || row(square) == numberOfRows - 1
}