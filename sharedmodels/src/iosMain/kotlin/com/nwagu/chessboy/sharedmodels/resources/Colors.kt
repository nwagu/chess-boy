package com.nwagu.chessboy.sharedmodels.resources

import com.nwagu.chess.model.SquareColor

actual typealias ColorRes = String

actual fun getPrimaryColor(): ColorRes = "red"
actual fun getPrimaryColorDark(): ColorRes = "black"
actual fun getPrimaryColorLight(): ColorRes = "gray"
actual fun getScreenBackgroundColor(): ColorRes = "white"
actual fun getBoardBackgroundColor(): ColorRes = "white"
actual fun SquareColor.colorResource(): ColorRes {
    return when (this) {
        SquareColor.WHITE -> "white"
        SquareColor.BLACK -> "black"
    }
}

