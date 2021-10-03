package com.nwagu.chessboy.model

data class ViewAction(
    val displayName: String,
    val action: () -> Unit
)