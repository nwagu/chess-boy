package com.nwagu.chess.model

import kotlinx.serialization.*

sealed class Move {
    abstract val source: Square
    abstract val destination: Square
    var isCapture: Boolean = false
    var san: String = ""
}

@Serializable
data class RegularMove(
    override val source: Square,
    override val destination: Square
): Move()

@Serializable
data class Promotion(
    override val source: Square,
    override val destination: Square,
    var promotionType: ChessPieceType
): Move()

@Serializable
data class EnPassant(
    override val source: Square,
    override val destination: Square
): Move()

@Serializable
data class Castling(
    // Castling here is considered primarily a king move
    override val source: Square, // the initial position of the king
    override val destination: Square // the final position of the king
): Move()