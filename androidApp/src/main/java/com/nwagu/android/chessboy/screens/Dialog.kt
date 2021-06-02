package com.nwagu.android.chessboy.screens

sealed class Dialog(val id: String) {
    object QuitGame : Dialog("Quit")
    object ResignGame : Dialog("Resign")
    object SelectPromotionPiece : Dialog("SelectPromotionPiece")
    object CheckMate : Dialog("Checkmate")
    object Draw : Dialog("Draw")
}
