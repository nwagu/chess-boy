package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.model.Board
import com.nwagu.chess.model.Move
import kotlinx.coroutines.flow.MutableStateFlow

actual class JWTC: UCIChessEngine() {
    override val id: String
        get() = TODO("Not yet implemented")
    override val name: String
        get() = TODO("Not yet implemented")

    override suspend fun getNextMove(board: Board): Move? {
        TODO("Not yet implemented")
    }

    override val minLevel: Int
        get() = TODO("Not yet implemented")
    override val maxLevel: Int
        get() = TODO("Not yet implemented")
    override var level: Int
        get() = TODO("Not yet implemented")
        set(value) {}
    override val connectionState: MutableStateFlow<Boolean>
        get() = TODO("Not yet implemented")

    override fun init() {
        TODO("Not yet implemented")
    }

    override fun quit() {
        TODO("Not yet implemented")
    }

}