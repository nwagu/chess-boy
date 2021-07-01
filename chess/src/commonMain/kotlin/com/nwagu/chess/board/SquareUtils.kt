package com.nwagu.chess.board

import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.enums.SquareColor

fun Board.square(row: Int, column: Int): Square = row * numberOfRows + column

fun Board.row(square: Square) = square / numberOfColumns
fun Board.column(square: Square) = square % numberOfColumns

fun Board.sum(square: Square) = row(square) + column(square)
fun Board.diff(square: Square) = row(square) - column(square)

fun Board.squareColor(square: Square): SquareColor {
    return if (sum(square) % 2 == 0) SquareColor.WHITE else SquareColor.BLACK
}

fun Board.areSquaresOnSameRow(a: Square, b: Square): Boolean {
    return row(a) == row(b)
}

fun Board.areSquaresOnSameColumn(a: Square, b: Square): Boolean {
    return column(a) == column(b)
}

fun Board.areSquaresOnSameDiagonal(a: Square, b: Square): Boolean {
    return diff(a) == diff(b) || sum(a) == sum(b)
}

fun Board.areSquaresClearOnSameRow(a: Square, b: Square): Boolean {
    return areSquaresOnSameRow(a, b)
            &&
            squaresBetweenSquaresOnRow(a, b).all {
                squaresMap[it] == EmptySquare
            }
}

fun Board.areSquaresClearOnSameColumn(a: Square, b: Square): Boolean {
    return areSquaresOnSameColumn(a, b)
            &&
            squaresBetweenSquaresOnColumn(a, b).all {
                squaresMap[it] == EmptySquare
            }
}

fun Board.areSquaresClearOnSameDiagonal(a: Square, b: Square): Boolean {
    return (areSquaresOnSameDiagonal(a, b))
            &&
            squaresBetweenSquaresOnDiagonal(a, b).all {
                squaresMap[it] == EmptySquare
            }
}

fun Board.squaresBetweenSquaresOnRow(a: Square, b: Square): IntArray {
    if (!areSquaresOnSameRow(a, b))
        throw IllegalArgumentException("The two squares must be in the same row!")

    return (minOf(a, b) + 1 until maxOf(a, b)).toList().toIntArray()
}

fun Board.squaresBetweenSquaresOnColumn(a: Square, b: Square): IntArray {
    if (!areSquaresOnSameColumn(a, b))
        throw IllegalArgumentException("The two squares must be in the same column!")

    return ((minOf(row(a), row(b)) + 1) until maxOf(row(a), row(b))).toList().map { row ->
        square(row, column(a))
    }.toIntArray()
}

fun Board.squaresBetweenSquaresOnDiagonal(a: Square, b: Square): IntArray {

    when {
        sum(a) == sum(b) -> {
            return ((minOf(row(a), row(b)) + 1) until maxOf(row(a), row(b))).toList().map { row ->
                val column = sum(a) - row
                (row * numberOfColumns) + column
            }.toIntArray()
        }
        diff(a) == diff(b) -> {
            return ((minOf(row(a), row(b)) + 1) until maxOf(row(a), row(b))).toList().map { row ->
                val column = row - diff(a)
                (row * numberOfColumns) + column
            }.toIntArray()
        }
        else -> throw IllegalArgumentException("The two squares must be in the same diagonal!")
    }

}

fun Board.squaresWithPiecesColored(color: ChessPieceColor): List<Int> {
    return squaresMap.filter {
        it.value.let {
            it is ChessPiece && it.chessPieceColor == color
        }
    }.map { it.key }
}

fun Board.squaresWithPiecesColoredAndType(color: ChessPieceColor, type: ChessPieceType): List<Int> {
    return squaresMap.filter {
        it.value.let {
            it is ChessPiece && it.chessPieceColor == color && it.chessPieceType == type
        }
    }.map { it.key }
}