package com.nwagu.chess.convention

import com.nwagu.chess.board.*
import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.enums.chessPieceTypeWithSanSymbol
import com.nwagu.chess.enums.opposite
import com.nwagu.chess.moves.*
import java.lang.StringBuilder

fun Board.getHistorySAN(): List<String> {
    return movesHistory.map { it.san }
}

fun Board.attachSanToMove(piece: ChessPiece, move: Move) {

    val san = StringBuilder()

    when {
        move is Castling -> {
            san.append(if (column(move.destination) > column(move.source)) "O-O" else "O-O-O")
        }
        (move is RegularMove) -> {
            san.append(piece.chessPieceType.sanSymbol)
            san.append(squareToCoordinate(move.source)) // no room for ambiguity!
            if (move.isCapture)
                san.append("x")
            san.append(squareToCoordinate(move.destination))
        }
        (move is EnPassant) -> {
            san.append(piece.chessPieceType.sanSymbol)
            san.append(squareToCoordinate(move.source)) // no room for ambiguity!
            san.append("x")
            san.append(squareToCoordinate(move.destination))
        }
        move is Promotion -> {
            san.append(piece.chessPieceType.sanSymbol)
            san.append(squareToCoordinate(move.source)) // no room for ambiguity!
            if (move.isCapture)
                san.append("x")
            san.append(squareToCoordinate(move.destination))
            san.append("=")
            san.append(move.promotionType.sanSymbol)
        }
    }

    if (isCheckMate(piece.chessPieceColor.opposite())) {
        san.append("#")
    } else if (isOnCheck(piece.chessPieceColor.opposite())) {
        san.append("+")
    }

    // TODO attach clock comment

    move.san = san.toString()
}

fun Board.sanToMove(san: String): Move { // Qh8g7

    val _san = san.takeWhile { it !in listOf('+', '#') }

    if (_san == "O-O") {
        if (turn == ChessPieceColor.WHITE)
            return Castling(whiteKingPosition, whiteKingPosition + 2)
                .also { it.san = san }
        else
            return Castling(blackKingPosition, blackKingPosition + 2)
                .also { it.san = san }
    }
    else if (_san == "O-O-O") {
        if (turn == ChessPieceColor.WHITE)
            return Castling(whiteKingPosition, whiteKingPosition - 2)
                .also { it.san = san }
        else
            return Castling(blackKingPosition, blackKingPosition - 2)
                .also { it.san = san }
    }

    val isCapture = _san.contains("x")
    val isPromotion = _san.contains("=")

    val moveCode = if (isPromotion) _san.split("=")[0] else _san

    val destinationLabel = moveCode.takeLast(2)
    val destination = coordinateToSquare(destinationLabel)

    val sourceSan = if (isCapture)
        moveCode.split("x")[0]
    else
        moveCode.removeSuffix(destinationLabel)

    val movingPieceType =
        try {
            chessPieceTypeWithSanSymbol(sourceSan.take(1))
        } catch (e: IllegalStateException) {
            ChessPieceType.PAWN
        }

    val ambiguityResolver = sourceSan.removePrefix(movingPieceType.sanSymbol)

    val source = when(ambiguityResolver.length) {
        0 -> {
            // no ambiguity expected
            getSquareOfPieceOfTypeThatCanMoveTo(movingPieceType, destination)
        }
        1 -> {
            // ambiguity resolver must be either the column or row of source
            if (ambiguityResolver.toIntOrNull() == null) {
                val column = fileToColumn(ambiguityResolver)
                getSquareOfPieceOfTypeThatCanMoveTo(movingPieceType, destination, column = column)
            } else {
                val row = rankToRow(ambiguityResolver)
                getSquareOfPieceOfTypeThatCanMoveTo(movingPieceType, destination, row = row)
            }
        }
        2 -> {
            // ambiguity resolver must be the coordinate of source
            coordinateToSquare(ambiguityResolver)
        }
        else -> throw IllegalStateException("Unexpected san format!")
    }

    if (isPromotion) {
        val promotionPieceType = chessPieceTypeWithSanSymbol(_san.split("=")[1])
        return Promotion(source, destination, promotionPieceType)
            .also {
                it.isCapture = isCapture
                it.san = san
            }
    } else if (isCapture && movingPieceType == ChessPieceType.PAWN && squareEmpty(destination)) {
        return EnPassant(source, destination)
            .also {
                it.isCapture = true
                it.san = san
            }
    } else {
        return RegularMove(source, destination)
            .also {
                it.isCapture = isCapture
                it.san = san
            }
    }
}

fun Board.getSquareOfPieceOfTypeThatCanMoveTo(
    pieceType: ChessPieceType,
    destination: Square,
    column: Int? = null,
    row: Int? = null
): Square {
    return squaresWithPiecesColoredAndType(turn, pieceType).filter { square ->
        (column?.let { it == column(square) } ?: true) &&
                (row?.let { it == row(square) } ?: true)
    }.first { square ->
        destination in getPossibleMovesFrom(square).map { it.destination }
    }
}