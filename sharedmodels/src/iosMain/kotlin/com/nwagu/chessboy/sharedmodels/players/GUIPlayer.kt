package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.Player
import com.nwagu.chessboy.sharedmodels.utils.ImageRes

actual abstract class GUIPlayer: Player {
    actual open var avatar: ImageRes = "default_avatar"
}