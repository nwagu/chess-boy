package com.nwagu.android.chessboy.model

data class QuickAction(
    val displayName: String,
    val action: () -> Unit
)