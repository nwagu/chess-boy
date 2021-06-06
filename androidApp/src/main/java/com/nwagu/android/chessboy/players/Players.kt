package com.nwagu.android.chessboy.players

import com.nwagu.android.chessboy.widgets.SelectableOpponent
import com.nwagu.chess.Player
import com.nwagu.chess.board.Board
import com.nwagu.chess.moves.Move

object User: Player {
    override val name = "You"
}

data class BluetoothOpponent(
    override val name: String = "Bluetooth Opponent",
    val address: String?,
): SelectableOpponent

interface AI: SelectableOpponent {
    override val name: String
    suspend fun getNextMove(board: Board): Move?
}