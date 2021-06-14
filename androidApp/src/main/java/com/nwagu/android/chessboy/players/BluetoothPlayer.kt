package com.nwagu.android.chessboy.players

import com.nwagu.android.chessboy.widgets.SelectablePlayer

data class BluetoothPlayer(
    override val name: String = "Bluetooth Opponent",
    val address: String,
    override val id: String = "${PlayersRegister.BLUETOOTH}-${address}"
): SelectablePlayer