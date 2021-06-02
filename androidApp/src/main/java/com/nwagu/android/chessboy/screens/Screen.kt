package com.nwagu.android.chessboy.screens

import androidx.annotation.StringRes
import com.nwagu.android.chessboy.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Home : Screen("Home", R.string.app_name)
    object NewGame : Screen("New Game", R.string.app_name)
    object NewBluetoothGame : Screen("New Bluetooth Game", R.string.app_name)
}
