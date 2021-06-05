package com.nwagu.chess.board

import com.nwagu.chess.moves.Move
import com.nwagu.chess.convention.populateCellsWithEmpties

typealias Square = Int

class Board(
    val numberOfRows: Int = 8,
    val numberOfColumns: Int = 8
) {

    val squaresMap: HashMap<Int, SquareOccupant> = hashMapOf()
    val movesHistory = ArrayDeque<Move>()
    val captives = ArrayDeque<ChessPiece>()
    var blackKingPosition: Int = 0
    var whiteKingPosition: Int = 0

    init {
        populateCellsWithEmpties()
        movesHistory.clear()
        captives.clear()
    }

    fun getSquareOccupant(index: Int): ChessPiece {
        return squaresMap[index] as ChessPiece
    }

    fun setSquareOccupant(index: Int, piece: SquareOccupant) {
        squaresMap[index] = piece
    }

}