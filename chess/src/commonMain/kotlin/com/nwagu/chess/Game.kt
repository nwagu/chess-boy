package com.nwagu.chess

import com.nwagu.chess.board.Board
import com.nwagu.chess.positions.setToStandardStartingPosition

class Game(
    val whitePlayer: Player,
    val blackPlayer: Player
) {

    var title = "${whitePlayer.name} (W) vs ${blackPlayer.name} (B)"

    val board: Board = Board()

    init {
        board.setToStandardStartingPosition()
    }

    private fun convertBoardPositionToString(): String {
        return board.squaresMap.map {
            "${it.key}=${it.value}"
        }.joinToString(",")
    }

    fun compareBoardPositionTo(position: String): Boolean {
        return (position == convertBoardPositionToString())
    }

}
