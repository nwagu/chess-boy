package com.nwagu.chessboy.sharedmodels.resources

import com.nwagu.chess.model.SquareColor

expect class ColorRes

expect fun getPrimaryColor(): ColorRes
expect fun getPrimaryColorDark(): ColorRes
expect fun getPrimaryColorLight(): ColorRes
expect fun getScreenBackgroundColor(): ColorRes
expect fun getBoardBackgroundColor(): ColorRes
expect fun SquareColor.colorResource(): ColorRes