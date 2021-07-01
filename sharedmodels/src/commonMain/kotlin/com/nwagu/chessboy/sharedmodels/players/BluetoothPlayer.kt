package com.nwagu.chessboy.sharedmodels.players

data class BluetoothPlayer(
    override val name: String = "Bluetooth Opponent",
    val address: String,
    override val id: String = "${PlayersRegister.BLUETOOTH}-${address}"
): SelectablePlayer()