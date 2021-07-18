package com.nwagu.bluetoothchat

import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

expect class BluetoothChatService() {
    var connectionState: MutableStateFlow<ConnectionState>
    var isInitiator: Boolean
    var partnerAddress: String

    fun init(isInitiator: Boolean)
    fun setListener(listener: ChatListener)
    fun connectDevice(address: String, secure: Boolean)
    fun startListeningForConnection()
    fun sendMessage(message: String)
    fun stopListening()
    fun stop()
}

// Name for the SDP record when creating server socket
val NAME_SECURE = "BluetoothChatSecure"
val NAME_INSECURE = "BluetoothChatInsecure"

// Unique UUID for this application
val MY_UUID_SECURE: UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")
val MY_UUID_INSECURE: UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")

enum class ConnectionState {
    NONE, LISTENING, CONNECTING, CONNECTED
}

interface ChatListener {
    fun onConnecting()
    fun onListening()
    fun onConnected(address: String)
    fun onChatStart(deviceName: String)
    fun onConnectionFailed()
    fun onMessageSent(message: String)
    fun onMessageReceived(message: String)
    fun onDisconnected()
}