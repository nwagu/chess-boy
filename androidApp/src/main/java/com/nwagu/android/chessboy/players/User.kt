package com.nwagu.android.chessboy.players

import com.nwagu.android.chessboy.R

object User: GUIPlayer() {
    override val id = PlayersRegister.USER
    override val name = "You"
    override val avatar = R.drawable.img_default_avatar
}