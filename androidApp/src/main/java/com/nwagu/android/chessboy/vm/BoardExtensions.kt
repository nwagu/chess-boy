package com.nwagu.android.chessboy.vm

import com.nwagu.android.chessboy.players.BluetoothOpponent
import com.nwagu.android.chessboy.players.User
import com.nwagu.chess.Game
import com.nwagu.chess.board.turn
import com.nwagu.chess.enums.ChessPieceColor

fun Game.isBluetoothGame(): Boolean {
    return this.blackPlayer is BluetoothOpponent || this.whitePlayer is BluetoothOpponent
}

val Game.isUserTurn: Boolean
    get() = when(board.turn) {
        ChessPieceColor.WHITE -> whitePlayer is User
        ChessPieceColor.BLACK -> blackPlayer is User
    }

val Game.userColor: ChessPieceColor
    get() = colorOnUserSideOfBoard

val Game.colorOnUserSideOfBoard: ChessPieceColor
    get() {
        return if (blackPlayer == User)
            ChessPieceColor.BLACK
        else
            ChessPieceColor.WHITE
    }

val Game.isBluetoothOpponentTurn: Boolean
    get() = when(board.turn) {
        ChessPieceColor.WHITE -> whitePlayer is BluetoothOpponent
        ChessPieceColor.BLACK -> blackPlayer is BluetoothOpponent
    }