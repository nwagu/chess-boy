package com.nwagu.bluetoothchat

import kotlinx.coroutines.flow.MutableStateFlow

actual class BluetoothChatService {

    private var _connectionState = ConnectionState.NONE
        set(value) {
            field = value
            connectionState.value = value
        }
    actual var connectionState = MutableStateFlow(_connectionState)

    actual var isInitiator: Boolean = false
    actual var partnerAddress: String = ""

    private lateinit var listener: ChatListener

    actual fun init(isInitiator: Boolean) {
        this.isInitiator = isInitiator
        // TODO
    }

    actual fun setListener(listener: ChatListener) {
        this.listener = listener
    }

    actual fun connectDevice(address: String, secure: Boolean) {
        // TODO
    }

    actual fun startListeningForConnection() {
        // TODO
    }

    actual fun sendMessage(message: String) {
        // TODO
    }

    actual fun stopListening() {
        // TODO
    }

    actual fun stop() {
        // TODO
    }

}