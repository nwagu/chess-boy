package com.nwagu.chess

import com.benasher44.uuid.uuid4
import com.nwagu.chess.board.Board
import com.nwagu.chess.convention.loadStandardStartingPosition

class Game(
    val id: String = uuid4().toString(),
    val whitePlayer: Player,
    val blackPlayer: Player
) {

    var title = "${whitePlayer.name} (W) vs ${blackPlayer.name} (B)"

    val board: Board = Board()

    init {
        board.loadStandardStartingPosition()
    }

}
