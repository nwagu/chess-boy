package com.nwagu.android.chessboy.players

import com.nwagu.android.chessboy.widgets.SelectableOpponent
import com.nwagu.chess.Player
import com.nwagu.chess.board.Board
import com.nwagu.chess.moves.Move

object User: Player {
    override val id = PlayersRegister.USER.id
    override val name = "You"
}

data class BluetoothOpponent(
    override val name: String = "Bluetooth Opponent",
    val address: String,
    override val id: String = "${PlayersRegister.BLUETOOTH.id}-${address}"
): SelectableOpponent

interface MoveGenerator: SelectableOpponent {
    override val name: String
    suspend fun getNextMove(board: Board): Move?
}

interface AI: MoveGenerator {
    var level: Int
    fun init()
}