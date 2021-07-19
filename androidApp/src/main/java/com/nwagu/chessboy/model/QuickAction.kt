package com.nwagu.chessboy.model

data class QuickAction(
    val displayName: String,
    val action: () -> Unit
)