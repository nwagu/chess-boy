package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.representation.getFen
import com.nwagu.chess.model.*
import com.nwagu.chessboy.sharedmodels.R
import com.nwagu.chessboy.sharedmodels.utils.ImageRes
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

        val move = jni.getMove()

        if (move == 0)
            return null

        val source: Int = getFrom(move)
        val destination: Int = getTo(move)

        if (isPromotionMove(move)) {
            val promotionPieceType = when (getPromotionPiece(move)) {
                BoardConstants.QUEEN -> ChessPieceType.QUEEN
                BoardConstants.KNIGHT -> ChessPieceType.KNIGHT
                BoardConstants.ROOK -> ChessPieceType.ROOK
                BoardConstants.BISHOP -> ChessPieceType.BISHOP
                else -> throw IllegalStateException("Invalid Promotion Piece!")
            }
            return Promotion(source, destination, promotionPieceType)
        }

        if (isEP(move)) {
            return EnPassant(source, destination)
        }

        if (isOOO(move)) {
            return Castling(source, destination)
        }

        if (isOO(move)) {
            return Castling(source, destination)
        }

        return RegularMove(source, destination)

    }

    override fun quit() {
        jni.destroy()
    }

}