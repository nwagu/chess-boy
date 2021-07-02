package com.nwagu.stockfish

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

class StockfishService : Service() {

    companion object {
        private const val TAG = "StockfishService"
        const val MSG_KEY = "MSG_KEY"

        init {
            System.loadLibrary("stockfish")
        }
    }

    private var mClient: Messenger? = null
    private val mMessenger: Messenger = Messenger(IncomingHandler())

    private inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            mClient = msg.replyTo
            if (mClient == null) {
                Log.e(TAG, "msg.replyTo is null")
                super.handleMessage(msg)
                return
            }
            val data = msg.peekData()
            if (data == null) {
                super.handleMessage(msg)
                return
            }
            val line = data.getString(MSG_KEY)
            if (line == null) {
                super.handleMessage(msg)
                return
            }
            clientToEngine(line)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return mMessenger.binder
    }

    /**
     * Called from Native code.
     * @param line a line sent by the engine
     */
    fun engineToClient(line: String?) {
        Log.v(TAG, "At engineToClient.")
        if (mClient != null) {
            val bundle = Bundle()
            bundle.classLoader = Thread.currentThread().contextClassLoader
            bundle.putString(MSG_KEY, line)
            val msg = Message.obtain()
            msg.data = bundle
            try {
                mClient!!.send(msg)
            } catch (e: RemoteException) {
                Log.d(TAG, "Client is not reachable.")
                mClient = null
            }
        }
    }

    /**
     * Implemented in native code. Call this to send commands to the engine.
     * @param line a command line to be sent to the engine
     */
    external fun clientToEngine(line: String?)
}