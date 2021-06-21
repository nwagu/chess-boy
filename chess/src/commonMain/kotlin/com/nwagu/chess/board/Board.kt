package com.nwagu.chess.board

import com.nwagu.chess.convention.getFen
import com.nwagu.chess.moves.Move

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
        reset()
    }

    fun reset() {
        movesHistory.clear()
        captives.clear()
        repeat(numberOfColumns * numberOfRows) { index ->
            squaresMap[index] = EmptySquare
        }
    }

    fun getSquareOccupant(square: Square): ChessPiece {
        return squaresMap[square] as ChessPiece
    }

    fun getSquareOccupantOrNull(square: Square): ChessPiece? {
        return if (squareEmpty(square)) null else squaresMap[square] as ChessPiece
    }

    fun setSquareOccupant(square: Square, piece: SquareOccupant) {
        squaresMap[square] = piece
    }

    fun compareTo(fen: String): Boolean {
        return (fen == getFen())
    }

}