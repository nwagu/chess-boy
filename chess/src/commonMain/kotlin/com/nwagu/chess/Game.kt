package com.nwagu.chess

import com.nwagu.chess.board.Board
import com.nwagu.chess.convention.loadStandardStartingPosition
import java.util.*

class Game(
    val id: String = UUID.randomUUID().toString(),
    val whitePlayer: Player,
    val blackPlayer: Player
) {

    var title = "${whitePlayer.name} (W) vs ${blackPlayer.name} (B)"

    val board: Board = Board()

    init {
        board.loadStandardStartingPosition()
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
