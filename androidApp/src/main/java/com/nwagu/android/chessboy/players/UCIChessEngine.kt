package com.nwagu.android.chessboy.players

import com.nwagu.chess.board.Board
import com.nwagu.chess.convention.convertChessEngineMoveToMove
import com.nwagu.chess.convention.getFen
import com.nwagu.chess.moves.Move
import com.nwagu.chessengineintegration.UCIBridge
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class UCIChessEngine(
    override val name: String,
    val pathToBinary: String
): AI {

    var level: Int = 5

    lateinit var uciBridge: UCIBridge

    private val chessEngineMove = MutableStateFlow("")

    fun init() {

        uciBridge = UCIBridge(pathToBinary, object : UCIBridge.UCIEngineListener {
            override fun onUciOk() {
                Timber.i("UCI OK")
            }

            override fun onReady() {
                Timber.i("UCI Ready")
            }

            override fun onBestMove(move: String) {
                Timber.i(move)
                chessEngineMove.value = move
            }

            override fun onCopyProtection() {
                Timber.i("copy protection")
            }

            override fun onId(id: Pair<String, String>) {
                Timber.i("ID received: ${id.first} = ${id.second}")
            }

            override fun onInfo(info: String) {
                Timber.i("INFO received: ${info}")
            }

            override fun onOption(option: Pair<String, String>) {
                Timber.i("Option received: ${option.first} = ${option.second}")
            }

            override fun onError(error: String) {
                Timber.e(error)
            }

        })

        uciBridge.init()
    }

    override suspend fun getNextMove(board: Board): Move? {

        var move: String? = null

        val job = GlobalScope.launch {
            chessEngineMove.collect {
                move = it
            }
        }

        uciBridge.play(board.getFen(), level)

        while (move == null) {
            //
        }

        job.cancel()

        return board.convertChessEngineMoveToMove(move ?: "")

    }

}