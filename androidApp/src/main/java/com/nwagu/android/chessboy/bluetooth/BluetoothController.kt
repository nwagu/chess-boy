package com.nwagu.android.chessboy.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import androidx.core.app.ActivityCompat.startActivityForResult
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.nwagu.android.chessboy.constants.RequestCodes
import com.nwagu.android.chessboy.receiver.DeviceFoundReceiver
import com.nwagu.android.chessboy.screens.main.MainActivity
import com.nwagu.chessboy.sharedmodels.presentation.ScanState
import timber.log.Timber

class BluetoothController(
    private val context: Activity
) {

    val viewModel by lazy { (context as MainActivity).newBluetoothGameViewModel }

    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    val receiver: DeviceFoundReceiver by lazy { DeviceFoundReceiver(viewModel) }

    val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled ?: false

    fun startBluetooth() {

        if (!isBluetoothEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(context, enableIntent, RequestCodes.REQUEST_ENABLE_BT, null)
        }

    }

    fun closeBluetooth() {

        if (bluetoothAdapter?.isEnabled == true) {
            bluetoothAdapter.disable()
        }
    }

    fun startDiscovery() {

        if (!isBluetoothEnabled) {
            startBluetooth()
            return
        }

        Dexter.withContext(context)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {

                override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                    if (bluetoothAdapter.isDiscovering) {
                        bluetoothAdapter.cancelDiscovery()
                    }
                    registerReceiver()
                    bluetoothAdapter.startDiscovery()
                    viewModel.scanState.value = ScanState.SCANNING
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    //
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    //
                }
            }).check()
    }

    fun endDiscovery() {
        bluetoothAdapter?.cancelDiscovery()
        unregisterReceiver()
        viewModel.scanState.value = ScanState.NONE
    }

    fun ensureDiscoverable() {

        if (bluetoothAdapter.scanMode != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            context.startActivity(discoverableIntent)
        }

    }

    private fun registerReceiver() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        val filter2 = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        context.registerReceiver(receiver, filter)
        context.registerReceiver(receiver, filter2)
    }

    private fun unregisterReceiver() {
        try {
            context.unregisterReceiver(receiver)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

}