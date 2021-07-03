package com.nwagu.android.chessboy.screens.navigation

sealed class Dialog(val route: String) {
    object SelectPromotionPiece : Dialog("selectPromotionPiece")
    object EnableLocation : Dialog("enableLocation")
}
