package com.nwagu.android.chessboy.players

import com.nwagu.chess.board.Board
import com.nwagu.chess.moves.Move

class Stockfish13 : UCIChessEngine {

    override val id = PlayersRegister.STOCKFISH13
    override val name = "Stockfish 13"
    override var level: Int = 5

    override fun init() {
        //
    }

    override suspend fun getNextMove(board: Board): Move? {
        return null
    }

}