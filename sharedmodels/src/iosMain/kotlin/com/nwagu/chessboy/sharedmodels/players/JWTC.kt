package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.model.Board
import com.nwagu.chess.model.Move
import com.nwagu.chess.representation.getFen
import com.nwagu.chessboy.sharedmodels.resources.ImageRes
import jwtc.chess.JWTCWrapper
import kotlinx.coroutines.flow.MutableStateFlow

// TODO move parameters that have same values to common sourceset
// https://youtrack.jetbrains.com/issue/KT-20427
actual class JWTC: UCIChessEngine() {

    override val id: String
        get() {
            return "${PlayersRegister.JWTC}-level=${level}"
        }

    override val name = "JWTC"
    override var avatar: ImageRes = "img_avatar_jwtc"
    override val minLevel = 1
    override val maxLevel = 7
    override var level = 5
    override val connectionState = MutableStateFlow(true)

    val jwtcWrapper = JWTCWrapper()

    override fun init() {
        jwtcWrapper.init()
    }

    override suspend fun getNextMove(board: Board): Move? {
        jwtcWrapper.init()
        jwtcWrapper.setFen(board.getFen())
        jwtcWrapper.setSearchDepth(level)
        return convertIntMove(jwtcWrapper.getMove())
    }

    override fun quit() {
        // todo
    }

}