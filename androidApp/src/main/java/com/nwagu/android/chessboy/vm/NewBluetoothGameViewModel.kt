package com.nwagu.android.chessboy.vm

import androidx.lifecycle.ViewModel
import com.nwagu.android.chessboy.model.data.BluetoothDevice
import com.nwagu.android.chessboy.players.BluetoothOpponent
import com.nwagu.chess.enums.ChessPieceColor
import kotlinx.coroutines.flow.MutableStateFlow

class NewBluetoothGameViewModel: ViewModel() {

    val selectedColor = MutableStateFlow(ChessPieceColor.WHITE)
    val selectedDevice = MutableStateFlow<BluetoothOpponent?>(null)

    val scanState = MutableStateFlow(ScanState.NONE)

    var discoveredDevices = MutableStateFlow<List<BluetoothDevice>>(listOf())

    fun onDeviceFound(device: BluetoothDevice) {
        if (device.address in discoveredDevices.value.map { it.address })
            return

        val newList = mutableListOf<BluetoothDevice>()
        newList.addAll(discoveredDevices.value)
        newList.add(device)
        discoveredDevices.value = newList
    }

    fun onScanFinished() {
        scanState.value = ScanState.SCAN_FINISHED
    }

}

enum class ScanState {
    NONE, SCANNING, SCAN_FINISHED
}