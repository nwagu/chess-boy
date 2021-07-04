package com.nwagu.chessboy.sharedmodels.resources

import com.nwagu.chess.model.SquareColor

actual typealias ColorRes = String

actual fun getPrimaryColor(): ColorRes = "Primary"
actual fun getPrimaryColorDark(): ColorRes = "PrimaryDark"
actual fun getPrimaryColorLight(): ColorRes = "PrimaryLight"
actual fun getScreenBackgroundColor(): ColorRes = "ScreenBackground"
actual fun getBoardBackgroundColor(): ColorRes = "BoardBackground"
actual fun SquareColor.colorResource(): ColorRes {
    return when (this) {
        SquareColor.WHITE -> "LightSquare"
        SquareColor.BLACK -> "DarkSquare"
    }
}

