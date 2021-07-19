package com.nwagu.chessboy.receiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nwagu.chessboy.sharedmodels.presentation.NewBluetoothGameViewModel

class DeviceFoundReceiver(
    private val viewModelNew: NewBluetoothGameViewModel
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action

        if (BluetoothDevice.ACTION_FOUND == action) {

            intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)?.let {
                val device = com.nwagu.bluetoothchat.model.BluetoothDevice(
                    address = it.address ?: return@let,
                    name = it.name ?: it.address,
                )
                viewModelNew.onDeviceFound(device)
            }

        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
            viewModelNew.onScanFinished()
        }
    }
}