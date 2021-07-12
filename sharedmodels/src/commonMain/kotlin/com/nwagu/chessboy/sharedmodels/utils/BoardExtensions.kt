package com.nwagu.chessboy.sharedmodels.utils

import com.nwagu.chess.gamelogic.isStaleMate
import com.nwagu.chess.model.Game
import com.nwagu.chess.gamelogic.turn
import com.nwagu.chess.model.Board
import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chess.model.Player
import com.nwagu.chessboy.sharedmodels.players.BluetoothPlayer
import com.nwagu.chessboy.sharedmodels.players.UCIChessEngine
import com.nwagu.chessboy.sharedmodels.players.User

fun Board.checkMessageForNextPlayer(): String {

    movesHistory.lastOrNull()?.let {
        val checkState = it.san.takeLast(1)
        if (checkState == "+") {
            return "Check"
        } else if (checkState == "#") {
            return "Checkmate"
        } else if (isStaleMate()) {
            return "Stalemate"
        }
    }

    return ""

}