package com.nwagu.chess.board

import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.enums.pieceSanSymbols
import com.nwagu.chess.moves.Move

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
    val sanX = san.takeWhile { it != '=' }

    if (type == ChessPieceType.PAWN) {
        return pieceSanSymbols().none {
            sanX.contains(it)
        }
    }

    return sanX.contains(type.sanSymbol)

}