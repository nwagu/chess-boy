package com.nwagu.android.chessboy.players

import com.nwagu.android.chessboy.widgets.SelectablePlayer
import com.nwagu.chess.board.Board
import com.nwagu.chess.moves.Move

interface MoveGenerator: SelectablePlayer {
    override val name: String
    suspend fun getNextMove(board: Board): Move?
}