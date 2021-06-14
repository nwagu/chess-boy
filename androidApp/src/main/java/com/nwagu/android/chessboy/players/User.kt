package com.nwagu.android.chessboy.players

import com.nwagu.chess.Player

object User: Player {
    override val id = PlayersRegister.USER
    override val name = "You"
}