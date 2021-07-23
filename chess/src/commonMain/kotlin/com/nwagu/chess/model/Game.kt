package com.nwagu.chess.model

import com.benasher44.uuid.uuid4
import com.nwagu.chess.representation.loadStandardStartingPosition

class Game(
    val id: String,
    val whitePlayer: Player,
    val blackPlayer: Player
) {
    // Using a second constructor (instead of default arguments)
    // in order to support initialization in swift
    constructor(
        whitePlayer: Player,
        blackPlayer: Player
    ): this(uuid4().toString(), whitePlayer, blackPlayer)

    var title = "${whitePlayer.name} (W) vs ${blackPlayer.name} (B)"

    val board: Board = Board()

    init {
        board.loadStandardStartingPosition()
    }

}
