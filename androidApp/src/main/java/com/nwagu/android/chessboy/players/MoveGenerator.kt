package com.nwagu.android.chessboy.players

import com.nwagu.android.chessboy.widgets.SelectablePlayer
import com.nwagu.chess.board.Board
import com.nwagu.chess.moves.Move

abstract class MoveGenerator: SelectablePlayer() {
    abstract override val name: String
    abstract suspend fun getNextMove(board: Board): Move?
}