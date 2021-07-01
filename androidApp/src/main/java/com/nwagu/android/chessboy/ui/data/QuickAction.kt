package com.nwagu.android.chessboy.ui.data

data class QuickAction(
    val displayName: String,
    val action: () -> Unit
)