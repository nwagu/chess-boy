package com.nwagu.android.chessboy.movesgenerators

import com.nwagu.android.chessboy.widgets.SelectableOpponent
import com.nwagu.chess.Player
import com.nwagu.chess.board.Board
import com.nwagu.chess.enums.Level
import com.nwagu.chess.moves.Move

object User: Player {
    override val name = "You"
    override val level = Level.INDETERMINATE
}

data class BluetoothOpponent(
    override val name: String = "Bluetooth Opponent",
    val address: String?,
    override val level: Level = Level.INDETERMINATE
): Player, SelectableOpponent {

    override val displayName = name
    override var selected = false
}

interface AI: Player, SelectableOpponent {
    override val name: String
    override val level: Level
    suspend fun getNextMove(board: Board): Move?
}