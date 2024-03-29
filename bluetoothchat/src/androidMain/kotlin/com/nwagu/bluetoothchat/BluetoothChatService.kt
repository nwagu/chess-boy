package com.nwagu.bluetoothchat

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.IOException
import java.util.*

actual class BluetoothChatService {

    // Debugging
    private val TAG = "BluetoothChatService"

    private var mSecureAcceptThread: AcceptThread? = null
    private var mInsecureAcceptThread: AcceptThread? = null
    private var mConnectThread: ConnectThread? = null
    private var mConnectedThread: ConnectedThread? = null

    private var _connectionState = ConnectionState.NONE
    set(value) {
        field = value
        connectionState.value = value
    }
    actual var connectionState = MutableStateFlow(_connectionState)

    private var bluetoothAdapter: BluetoothAdapter? = null

    actual var isInitiator: Boolean = false
    actual var partnerAddress: String = ""

    private lateinit var listener: ChatListener

    actual fun init(isInitiator: Boolean) {
        this.isInitiator = isInitiator
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    actual fun setListener(listener: ChatListener) {
        this.listener = listener
    }

    actual fun connectDevice(address: String, secure: Boolean) {

        if (!isInitiator)
            throw IllegalStateException("A non-initiator must only listen. Use [startListeningForConnection] to listen for connection.")

        partnerAddress = address

        val device = bluetoothAdapter?.getRemoteDevice(address) ?: return

        // Cancel any thread attempting to make a connection
        if (_connectionState == ConnectionState.CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread?.cancel()
                mConnectThread = null
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }

        // Start the thread to connect with the given device
        mConnectThread = ConnectThread(device, secure)
        mConnectThread?.start()
    }

    actual fun startListeningForConnection() {

        if (isInitiator)
            throw IllegalStateException("An initiator must not listen for connection. Use [connectDevice] to connect")

        if (_connectionState == ConnectionState.NONE) {
            startAcceptThread()
        }
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    @Synchronized
    fun startAcceptThread() {
        Log.d(TAG, "start accept thread")

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread?.cancel()
            mConnectThread = null
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (mSecureAcceptThread == null) {
            mSecureAcceptThread = AcceptThread(true)
            mSecureAcceptThread?.start()
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = AcceptThread(false)
            mInsecureAcceptThread?.start()
        }
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    @Synchronized
    private fun startChatThread(socket: BluetoothSocket, device: BluetoothDevice, socketType: String) {
        Log.d(TAG, "connected, Socket Type:$socketType")

        partnerAddress = device.address

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread?.cancel()
            mConnectThread = null
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }

        // Cancel the accept thread because we only want to connect to one device
        if (mSecureAcceptThread != null) {
            mSecureAcceptThread?.cancel()
            mSecureAcceptThread = null
        }
        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread?.cancel()
            mInsecureAcceptThread = null
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = ConnectedThread(socket, socketType)
        mConnectedThread?.start()
        listener.onConnected(device.address)

        listener.onChatStart(device.name)
    }

    actual fun sendMessage(message: String) {

        if (message.isEmpty())
            return

        synchronized (this) {
            if (_connectionState != ConnectionState.CONNECTED)
                return
            mConnectedThread?.write(message)
        }

    }

    actual fun stopListening() {
        if (mSecureAcceptThread != null) {
            mSecureAcceptThread?.cancel()
            mSecureAcceptThread = null
        }
        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread?.cancel()
            mInsecureAcceptThread = null
        }
        if (mConnectedThread == null) {
            _connectionState = ConnectionState.NONE
        }
    }

    /**
     * Stop all threads
     */
    @Synchronized
    actual fun stop() {
        Log.d(TAG, "stop")
        if (mConnectThread != null) {
            mConnectThread?.cancel()
            mConnectThread = null
        }
        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }
        if (mSecureAcceptThread != null) {
            mSecureAcceptThread?.cancel()
            mSecureAcceptThread = null
        }
        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread?.cancel()
            mInsecureAcceptThread = null
        }
        _connectionState = ConnectionState.NONE
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    inner class ConnectThread internal constructor(
        private val mmDevice: BluetoothDevice,
        secure: Boolean
    ) : Thread() {

        private val mmSocket = if (secure) {
            mmDevice.createRfcommSocketToServiceRecord(
                MY_UUID_SECURE
            )
        } else {
            mmDevice.createInsecureRfcommSocketToServiceRecord(
                MY_UUID_INSECURE
            )
        }
        private val mSocketType = if (secure) "Secure" else "Insecure"

        init {
            _connectionState = ConnectionState.CONNECTING
        }

        override fun run() {
            listener.onConnecting()

            Log.i(TAG, "BEGIN mConnectThread SocketType:$mSocketType")
            name = "ConnectThread$mSocketType"

            // Always cancel discovery because it will slow down a connection
            bluetoothAdapter?.cancelDiscovery()

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket?.connect()
            } catch (e: IOException) {
                // Close the socket
                try {
                    mmSocket?.close()
                } catch (e2: IOException) {
                    Log.e(
                        TAG, "unable to close() " + mSocketType +
                                " socket during connection failure", e2
                    )
                }
                listener.onConnectionFailed()
                _connectionState = ConnectionState.NONE
                return
            }

            // Reset the ConnectThread because we're done
            synchronized(this@BluetoothChatService) { mConnectThread = null }

            // Start the connected thread
            startChatThread(mmSocket, mmDevice, mSocketType)
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "close() of connect $mSocketType socket failed", e)
            }
        }

    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    inner class AcceptThread internal constructor(secure: Boolean) : Thread() {

        private val socket = if (secure) {
            bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                NAME_SECURE,
                MY_UUID_SECURE
            )
        } else {
            bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(
                NAME_INSECURE, MY_UUID_INSECURE
            )
        }
        private val socketType = if (secure) "Secure" else "Insecure"

        init {
            _connectionState = ConnectionState.LISTENING
        }

        override fun run() {

            listener.onListening()

            Log.d(
                TAG, "Socket Type: " + socketType +
                        "BEGIN mAcceptThread" + this
            )
            name = "AcceptThread$socketType"
            var socket: BluetoothSocket?

            // Listen to the server socket if we're not connected
            while (_connectionState != ConnectionState.CONNECTED) {
                socket = try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    this.socket?.accept()
                } catch (e: IOException) {
                    Log.e(TAG, "Socket Type: " + socketType + "accept() failed", e)
                    break
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized(this@BluetoothChatService) {
                        when (_connectionState) {
                            ConnectionState.LISTENING, ConnectionState.CONNECTING ->
                                // Situation normal. Start the connected thread.
                                startChatThread(
                                    socket, socket.remoteDevice,
                                    socketType
                                )
                            ConnectionState.NONE, ConnectionState.CONNECTED ->
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close()
                                } catch (e: IOException) {
                                    Log.e(TAG, "Could not close unwanted socket", e)
                                }
                        }
                    }
                }
            }
            Log.i(TAG, "END mAcceptThread, socket Type: $socketType")
        }

        fun cancel() {
            Log.d(TAG, "Socket Type" + socketType + "cancel " + this)
            try {
                socket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Socket Type" + socketType + "close() of server failed", e)
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    inner class ConnectedThread internal constructor(
        val socket: BluetoothSocket,
        socketType: String
    ) : Thread() {

        private val mmInStream = socket.inputStream
        private val mmOutStream = socket.outputStream

        init {
            Log.d(TAG, "create ConnectedThread: $socketType")
            _connectionState = ConnectionState.CONNECTED
        }

        override fun run() {

            Log.i(TAG, "BEGIN mConnectedThread")
            val buffer = ByteArray(1024)
            var bytes: Int

            // Keep listening to the InputStream while connected
            while (_connectionState == ConnectionState.CONNECTED) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer)

                    val `in` = String(buffer)
                    val messageLengthPrefix = `in`.split("###").first()
                    val messageLength = messageLengthPrefix.toInt()
                    val message = `in`.drop(messageLengthPrefix.length + 3).take(messageLength)

                    listener.onMessageReceived(message)

                } catch (e: IOException) {
                    Log.e(TAG, "disconnected", e)
                    listener.onDisconnected()
                    _connectionState = ConnectionState.NONE
                    break
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        fun write(message: String) {
            try {
                val out = "${message.length}###$message"
                mmOutStream.write(out.toByteArray())
                listener.onMessageSent(message)
            } catch (e: IOException) {
                Log.e(TAG, "Exception during write", e)
            }
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e: IOException) {
                Log.e(TAG, "close() of connect socket failed", e)
            }
        }

    }

}