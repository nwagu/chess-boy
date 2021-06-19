package com.nwagu.android.chessboy.ui.data

import androidx.annotation.DrawableRes

data class QuickAction(
    val displayName: String,
    @DrawableRes val image: Int,
    val action: () -> Unit
)