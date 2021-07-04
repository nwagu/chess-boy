package com.nwagu.chessboy.sharedmodels.resources

import com.nwagu.chess.model.SquareColor
import com.nwagu.chessboy.sharedmodels.R

actual typealias ColorRes = Int

actual fun getPrimaryColor(): ColorRes = R.color.colorPrimary
actual fun getPrimaryColorDark(): ColorRes = R.color.colorPrimaryDark
actual fun getPrimaryColorLight(): ColorRes = R.color.colorPrimaryLight
actual fun getBoardBackgroundColor(): ColorRes = R.color.screenBackground
actual fun getScreenBackgroundColor(): ColorRes = R.color.screenBackground
actual fun SquareColor.colorResource(): ColorRes {
    return when (this) {
        SquareColor.WHITE -> R.color.lightSquare
        SquareColor.BLACK -> R.color.darkSquare
    }
}