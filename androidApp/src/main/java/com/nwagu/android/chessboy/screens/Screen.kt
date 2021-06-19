package com.nwagu.android.chessboy.screens

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object NewGame : Screen("New Game")
    object NewBluetoothGame : Screen("New Bluetooth Game")
    object History : Screen("History")
    object Settings : Screen("Settings")
}
