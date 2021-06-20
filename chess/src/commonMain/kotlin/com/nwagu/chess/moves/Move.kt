package com.nwagu.chess.moves

import com.nwagu.chess.board.Square
import com.nwagu.chess.enums.ChessPieceType

sealed class Move {
    abstract val source: Square
    abstract val destination: Square
    var isCapture: Boolean = false
    var san: String = ""
}

data class RegularMove(
    override val source: Square,
    override val destination: Square
): Move()

data class Promotion(
    override val source: Square,
    override val destination: Square,
    var promotionType: ChessPieceType
): Move()

data class EnPassant(
    override val source: Square,
    override val destination: Square
): Move()

data class Castling(
    // Castling here is considered primarily a king move
    override val source: Square, // the initial position of the king
    override val destination: Square // the final position of the king
): Move()