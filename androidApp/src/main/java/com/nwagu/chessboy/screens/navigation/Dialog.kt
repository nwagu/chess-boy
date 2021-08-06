package com.nwagu.chessboy.screens.navigation

sealed class Dialog(val route: String) {
    object SelectPromotionPiece : Dialog("selectPromotionPiece")
    object EnableLocation : Dialog("enableLocation")
    object ConfirmDeleteGame : Dialog("confirmDeleteGame")
}
