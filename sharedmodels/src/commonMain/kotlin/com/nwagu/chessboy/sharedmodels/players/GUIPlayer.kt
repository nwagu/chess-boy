package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.Player
import com.nwagu.chessboy.sharedmodels.utils.ImageRes

expect abstract class GUIPlayer(): Player {
    open var avatar: ImageRes
}