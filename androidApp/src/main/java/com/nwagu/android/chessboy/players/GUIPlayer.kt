package com.nwagu.android.chessboy.players

import androidx.annotation.DrawableRes
import com.nwagu.android.chessboy.R
import com.nwagu.chess.Player

abstract class GUIPlayer: Player {
    @DrawableRes
    open val avatar: Int = R.drawable.img_default_avatar
}