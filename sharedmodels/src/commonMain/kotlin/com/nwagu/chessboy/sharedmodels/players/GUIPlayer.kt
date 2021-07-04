package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.model.Player
import com.nwagu.chessboy.sharedmodels.resources.ImageRes
import com.nwagu.chessboy.sharedmodels.resources.getDefaultPlayerAvatar

abstract class GUIPlayer: Player {
    open var avatar: ImageRes = getDefaultPlayerAvatar()
}