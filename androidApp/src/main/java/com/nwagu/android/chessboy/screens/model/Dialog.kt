package com.nwagu.android.chessboy.screens.model

sealed class Dialog(val id: String) {
    object SelectPromotionPiece : Dialog("SelectPromotionPiece")
    object EnableLocationDialog : Dialog("EnableLocation")
}