package com.nwagu.chessboy.model

data class LightAction(
    val displayName: String,
    val action: () -> Unit
)