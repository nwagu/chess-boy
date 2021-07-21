package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.model.Board
import com.nwagu.chess.model.Move
import com.nwagu.chessboy.sharedmodels.resources.ImageRes
import kotlinx.coroutines.flow.MutableStateFlow

// TODO move parameters that have same values to common sourceset
// https://youtrack.jetbrains.com/issue/KT-20427
actual class Stockfish : UCIChessEngine() {

    override val id: String
        get() {
            return "${PlayersRegister.STOCKFISH}-level=${level}"
        }

    override val name = "Stockfish"
    override var avatar: ImageRes = "img_avatar_stockfish"
    override val minLevel = 1
    override val maxLevel = 10
    override var level: Int = 5

    override val connectionState = MutableStateFlow(false)

    override fun init() {
        // todo
    }

    override suspend fun getNextMove(board: Board): Move? {
        // todo
        return null
    }

    override fun quit() {
        // todo
    }

}