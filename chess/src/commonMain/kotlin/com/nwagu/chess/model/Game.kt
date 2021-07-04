package com.nwagu.chess.model

import com.benasher44.uuid.uuid4
import com.nwagu.chess.representation.loadStandardStartingPosition

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
