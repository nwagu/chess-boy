package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chessboy.sharedmodels.resources.ImageRes
import com.nwagu.chessboy.sharedmodels.resources.getBluetoothPlayerAvatar

data class BluetoothPlayer(
    override val name: String = "Bluetooth Opponent",
    override var avatar: ImageRes = getBluetoothPlayerAvatar(),
    val address: String,
    override val id: String = "${PlayersRegister.BLUETOOTH}-${address}"
): SelectablePlayer()