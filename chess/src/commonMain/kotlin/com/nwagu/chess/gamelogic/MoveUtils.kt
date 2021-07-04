package com.nwagu.chess.gamelogic

import com.nwagu.chess.model.Board
import com.nwagu.chess.model.ChessPieceType
import com.nwagu.chess.model.pieceSanSymbols
import com.nwagu.chess.model.Move

val Board.halfMoveCountSinceLastCaptureOrPawnAdvance: Int
    get() {
        val lastCaptureOrPawnMove = movesHistory.lastOrNull {
            it.isCapture || it.isByType(ChessPieceType.PAWN)
        }

        return (movesHistory.size - 1) - (movesHistory.indexOf(lastCaptureOrPawnMove))
    }

val Board.fullMovesNumber: Int
    get() {
        return (movesHistory.count() / 2) + 1
    }

fun Move.isByType(type: ChessPieceType): Boolean {

    if (san in listOf("O-O", "O-O-O") && type in listOf(ChessPieceType.KING, ChessPieceType.ROOK))
        return true

    // remove promotion piece san symbol in case of promotion
    val _san = san.takeWhile { it != '=' }

    if (type == ChessPieceType.PAWN) {
        return pieceSanSymbols().none {
            _san.contains(it)
        }
    }

    return _san.contains(type.sanSymbol)

}