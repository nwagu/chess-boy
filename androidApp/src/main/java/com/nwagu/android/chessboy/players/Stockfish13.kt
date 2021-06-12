package com.nwagu.android.chessboy.players

import com.nwagu.chess.board.Board
import com.nwagu.chess.moves.Move
import com.nwagu.stockfish13.stockfish13WrapperJNI

class Stockfish13 : UCIChessEngine {

    override val id = PlayersRegister.STOCKFISH13
    override val name = "Stockfish 13"
    override var level: Int = 5

    lateinit var jni: stockfish13WrapperJNI

    init {
        System.loadLibrary("stockfish13Wrapper");
    }

    override fun init() {
        jni = stockfish13WrapperJNI()
    }

    override suspend fun getNextMove(board: Board): Move? {
        //stockfish13WrapperJNI.next_move(true)
        return null
    }

}