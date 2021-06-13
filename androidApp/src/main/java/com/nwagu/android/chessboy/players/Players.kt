package com.nwagu.android.chessboy.players

import com.nwagu.android.chessboy.widgets.SelectableOpponent
import com.nwagu.chess.Player
import com.nwagu.chess.board.Board
import com.nwagu.chess.moves.Move
import kotlinx.coroutines.flow.MutableStateFlow

object User: Player {
    override val id = PlayersRegister.USER
    override val name = "You"
}

data class BluetoothOpponent(
    override val name: String = "Bluetooth Opponent",
    val address: String,
    override val id: String = "${PlayersRegister.BLUETOOTH}-${address}"
): SelectableOpponent

interface MoveGenerator: SelectableOpponent {
    override val name: String
    suspend fun getNextMove(board: Board): Move?
}

interface UCIChessEngine: MoveGenerator {
    var level: Int
    val connectionState: MutableStateFlow<Boolean>
    fun init()
}