package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.model.Board
import com.nwagu.chess.model.Move

abstract class MoveGenerator: SelectablePlayer() {
    abstract override val name: String
    abstract suspend fun getNextMove(board: Board): Move?
}