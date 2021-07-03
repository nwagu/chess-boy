package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.model.Player
import com.nwagu.chessboy.sharedmodels.utils.ImageRes
import com.nwagu.chessboy.sharedmodels.utils.getDefaultPlayerAvatar

abstract class GUIPlayer: Player {
    open var avatar: ImageRes = getDefaultPlayerAvatar()
}