package com.nwagu.android.chessboy.screens.navigation

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object NewGame : Screen("New Game")
    object NewBluetoothGame : Screen("New Bluetooth Game")
    object GameAnalysis : Screen("Game Analysis")
    object History : Screen("History")
    object Settings : Screen("Settings")
}
