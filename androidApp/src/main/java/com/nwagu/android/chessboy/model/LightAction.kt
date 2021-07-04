package com.nwagu.android.chessboy.model

data class LightAction(
    val displayName: String,
    val action: () -> Unit
)