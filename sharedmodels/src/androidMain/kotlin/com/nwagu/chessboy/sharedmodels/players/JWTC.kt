package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.representation.getFen
import com.nwagu.chess.model.*
import com.nwagu.chessboy.sharedmodels.R
import com.nwagu.chessboy.sharedmodels.resources.ImageRes
import jwtc.chess.BoardConstants
import jwtc.chess.JNI
import jwtc.chess.Move.getFrom
import jwtc.chess.Move.getPromotionPiece
import jwtc.chess.Move.getTo
import jwtc.chess.Move.isEP
import jwtc.chess.Move.isOO
import jwtc.chess.Move.isOOO
import jwtc.chess.Move.isPromotionMove
import kotlinx.coroutines.flow.MutableStateFlow

// TODO move parameters that have same values to common sourceset
// https://youtrack.jetbrains.com/issue/KT-20427
actual class JWTC: UCIChessEngine() {

    override val id: String
        get() {
            return "${PlayersRegister.JWTC}-level=${level}"
        }

    override val name = "JWTC"
    override var avatar: ImageRes = R.drawable.img_avatar_jwtc
    override val minLevel = 1
    override val maxLevel = 7
    override var level = 5
    override val connectionState = MutableStateFlow(true)

    lateinit var jni: JNI

    override fun init() {
        jni = JNI()
    }

    override suspend fun getNextMove(board: Board): Move? {

        jni.newGame()
        jni.initFEN(board.getFen())
        jni.searchDepth(level)

        return convertIntMove(jni.getMove())

    }

    override fun quit() {
        jni.destroy()
    }

}