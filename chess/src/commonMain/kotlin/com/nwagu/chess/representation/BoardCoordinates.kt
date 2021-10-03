package com.nwagu.chess.representation

import com.nwagu.chess.gamelogic.*
import com.nwagu.chess.model.Board
import com.nwagu.chess.model.Square

fun Board.squareToCoordinate(square: Square): String {

    if (numberOfColumns != 8 || numberOfRows != 8)
        throw IllegalStateException("Standard square name requires an 8x8 board!")

    return "${columnToFile(column(square))}${rowToRank(row(square))}"

}

fun Board.rowToRank(row: Int): String {
    return "${numberOfRows - row}"
}

fun Board.columnToFile(column: Int): String {

    return when(column) {
        0 -> "a"
        1 -> "b"
        2 -> "c"
        3 -> "d"
        4 -> "e"
        5 -> "f"
        6 -> "g"
        7 -> "h"
        else -> throw IllegalStateException("Standard square name requires an 8x8 board!")
    }

}

fun Board.coordinateToSquare(coordinate: String): Square {
    return square(rankToRow(coordinate.takeLast(1)), fileToColumn(coordinate.take(1)))
}

fun Board.rankToRow(rank: String): Int {
    return numberOfRows - (rank.toInt())
}

fun Board.fileToColumn(file: String): Int {
    return when(file) {
        "a" -> 0
        "b" -> 1
        "c" -> 2
        "d" -> 3
        "e" -> 4
        "f" -> 5
        "g" -> 6
        "h" -> 7
        else -> throw IllegalStateException("Standard square name requires an 8x8 board!")
    }
}