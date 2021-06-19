package com.nwagu.android.chessboy.util

import com.nwagu.android.chessboy.players.BluetoothPlayer
import com.nwagu.android.chessboy.players.User
import com.nwagu.chess.Game
import com.nwagu.chess.board.turn
import com.nwagu.chess.enums.ChessPieceColor

fun Game.isBluetoothGame(): Boolean {
    return this.blackPlayer is BluetoothPlayer || this.whitePlayer is BluetoothPlayer
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
        ChessPieceColor.WHITE -> whitePlayer is BluetoothPlayer
        ChessPieceColor.BLACK -> blackPlayer is BluetoothPlayer
    }