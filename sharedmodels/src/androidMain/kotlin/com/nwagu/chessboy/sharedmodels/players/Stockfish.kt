package com.nwagu.chessboy.sharedmodels.players

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.widget.Toast
import co.touchlab.kermit.Kermit
import com.nwagu.chess.model.Board
import com.nwagu.chess.representation.parseUciEngineMove
import com.nwagu.chess.representation.getFen
import com.nwagu.chess.model.Move
import com.nwagu.chessboy.sharedmodels.ChessApplication
import com.nwagu.chessboy.sharedmodels.R
import com.nwagu.chessboy.sharedmodels.resources.ImageRes
import com.nwagu.stockfish.StockfishService
import com.nwagu.stockfish.StockfishService.Companion.MSG_KEY
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO move parameters that have same values to common sourceset
// https://youtrack.jetbrains.com/issue/KT-20427
actual class Stockfish(val context: Context) : UCIChessEngine() {

    actual constructor(): this(ChessApplication.context!!.applicationContext)

    override val id: String
        get() {
            return "${PlayersRegister.STOCKFISH}-level=${level}"
        }

    override val name = "Stockfish"
    override var avatar: ImageRes = R.drawable.img_avatar_stockfish
    override val minLevel = 1
    override val maxLevel = 10
    override var level: Int = 5

    private val chessEngineMove = MutableStateFlow("")

    override val connectionState = MutableStateFlow(false)
    var mService: Messenger? = null
    set(value) {
        field = value
        connectionState.value = value != null
    }

    override fun init() {
        val intent = Intent(context, StockfishService::class.java)
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override suspend fun getNextMove(board: Board): Move? {

        val lastMove = chessEngineMove.value
        var move = chessEngineMove.value

        val job = GlobalScope.launch {
            chessEngineMove.collect {
                move = it
            }
        }

        sendCommand("ucinewgame")
        delay(200)
        sendCommand("position fen ${board.getFen()}")
        delay(200)
        sendCommand("go depth $level")

        while (move == lastMove) {
            delay(200)
        }

        job.cancel()

        chessEngineMove.value = ""

        return board.parseUciEngineMove(move)

    }

    /*
    * Also appends newline to the command
    * */
    fun sendCommand(message: String) {
        if (mService == null) {
            return
        }
        val bundle = Bundle()
        bundle.putString(MSG_KEY, message + "\n")
        val msg = Message.obtain()
        msg.replyTo = mMessenger
        msg.data = bundle
        try {
            mService!!.send(msg)
        } catch (e: RemoteException) {
            mService = null
        }
    }

    /**
     * Handler of incoming messages from service.
     */
    inner class IncomingHandler : Handler() {

        override fun handleMessage(msg: Message) {
            val data = msg.peekData()
            if (data == null) {
                super.handleMessage(msg)
                return
            }
            data.classLoader = Thread.currentThread().contextClassLoader
            val line = data.getString(MSG_KEY)
            if (line == null) {
                super.handleMessage(msg)
                return
            }

            when {
                line.startsWith("id") -> {
                    // TODO Parse id
                    Kermit().i { "ID received: $line" }
                }
                line.startsWith("option") -> {
                    // TODO Parse options
                    Kermit().i { "Option received: $line" }
                }
                line.startsWith("uciok") -> {
                    Kermit().i { "UCI OK" }
                }
                line.startsWith("copyprotection") -> {
                    Kermit().i { "copy protection" }
                }
                line.startsWith("readyok") -> {
                    Kermit().i { "UCI Ready" }
                }
                line.startsWith("info") -> {
                    // TODO Parse info
                    Kermit().i { "INFO received: $line" }
                }
                line.startsWith("bestmove") -> {
                    val move = line
                        .removePrefix("bestmove ")
                        .trimStart()
                        .takeWhile { it != ' ' && it != '\n' }
                    if (uciMovePattern.matches(move)) {
                        Kermit().i { move }
                        chessEngineMove.value = move
                    }
                }
            }
        }
    }

    override fun quit() {
        if (mService != null) {
            context.unbindService(mConnection)
            mService = null
        }
    }

    val mMessenger: Messenger = Messenger(IncomingHandler())

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            mService = Messenger(service)
            Toast.makeText(context, "Engine connected.", Toast.LENGTH_LONG).show()
        }

        override fun onServiceDisconnected(className: ComponentName) {
            mService = null
            Toast.makeText(context, "Engine disconnected.", Toast.LENGTH_LONG).show()
        }
    }

}