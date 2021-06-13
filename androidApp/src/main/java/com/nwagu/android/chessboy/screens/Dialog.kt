package com.nwagu.android.chessboy.screens

sealed class Dialog(val id: String) {
    object SelectPromotionPiece : Dialog("SelectPromotionPiece")
}
