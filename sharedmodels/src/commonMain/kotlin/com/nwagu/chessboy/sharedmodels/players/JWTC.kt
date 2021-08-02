package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.model.*
import jwtc.chess.BoardConstants
import jwtc.chess.Move.getFrom
import jwtc.chess.Move.getPromotionPiece
import jwtc.chess.Move.getTo
import jwtc.chess.Move.isEP
import jwtc.chess.Move.isOO
import jwtc.chess.Move.isOOO
import jwtc.chess.Move.isPromotionMove

expect class JWTC(): UCIChessEngine

fun convertIntMove(move: Int): Move? {
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