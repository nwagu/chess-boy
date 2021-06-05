package com.nwagu.chess.board

import com.nwagu.chess.convention.attachSanToMove
import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.moves.*

fun Board.move(move: Move, attachSan: Boolean = true): Boolean {

    val movingPiece = try {
        getSquareOccupant(move.source)
    } catch (e: Exception) {
        return false
    }

    when(move) {
        is RegularMove -> {
            movingPiece.numberOfMovesMade++
            squaresMap[move.destination].let { captive ->
                if (captive is ChessPiece) {
                    move.isCapture = true
                    captives.add(captive)
                }
            }
            setSquareOccupant(move.source, EmptySquare)
            setSquareOccupant(move.destination, movingPiece)
        }
        is Promotion -> {
            val promotionPiece = ChessPiece(
                chessPieceType = move.promotionType,
                chessPieceColor = movingPiece.chessPieceColor,
                numberOfMovesMade = movingPiece.numberOfMovesMade++,
                startingRow = movingPiece.startingRow,
                startingColumn = movingPiece.startingColumn
            )
            squaresMap[move.destination].let { captive ->
                if (captive is ChessPiece) {
                    move.isCapture = true
                    captives.add(captive)
                }
            }
            setSquareOccupant(move.source, EmptySquare)
            setSquareOccupant(move.destination, promotionPiece)
        }
        is EnPassant -> {
            val captivePosition = square(row(move.source), column(move.destination))
            move.isCapture = true
            movingPiece.numberOfMovesMade++
            captives.add(getSquareOccupant(captivePosition))
            setSquareOccupant(move.source, EmptySquare)
            setSquareOccupant(move.destination, movingPiece)
            setSquareOccupant(captivePosition, EmptySquare)
        }
        is Castling -> {
            val rook = getSquareOccupant(move.secondarySource)

            movingPiece.numberOfMovesMade++
            rook.numberOfMovesMade++

            setSquareOccupant(move.source, EmptySquare)
            setSquareOccupant(move.destination, movingPiece)

            setSquareOccupant(move.secondarySource, EmptySquare)
            setSquareOccupant(move.secondaryDestination, rook)
        }
    }

    if (attachSan)
        attachSanToMove(movingPiece, move)

    movesHistory.add(move)

    if (movingPiece.chessPieceType == ChessPieceType.KING) {
        when (movingPiece.chessPieceColor) {
            ChessPieceColor.BLACK -> blackKingPosition = move.destination
            ChessPieceColor.WHITE -> whiteKingPosition = move.destination
        }
    }

    return true
}

fun Board.undoMove(): Move? {
    val move = movesHistory.removeLastOrNull() ?: return null

    val movingPiece = try {
        getSquareOccupant(move.destination)
    } catch (e: Exception) {
        return null
    }

    when(move) {
        is RegularMove -> {
            movingPiece.numberOfMovesMade--
            setSquareOccupant(move.source, movingPiece)

            if (move.isCapture)
                setSquareOccupant(move.destination, captives.removeLast())
            else
                setSquareOccupant(move.destination, EmptySquare)
        }
        is Promotion -> {
            val initialPawn = ChessPiece(
                chessPieceType = ChessPieceType.PAWN,
                chessPieceColor = movingPiece.chessPieceColor,
                numberOfMovesMade = movingPiece.numberOfMovesMade--,
                startingRow = movingPiece.startingRow,
                startingColumn = movingPiece.startingColumn
            )
            setSquareOccupant(move.source, initialPawn)

            if (move.isCapture)
                setSquareOccupant(move.destination, captives.removeLast())
            else
                setSquareOccupant(move.destination, EmptySquare)
        }
        is EnPassant -> {
            val captivePosition = square(row(move.source), column(move.destination))
            movingPiece.numberOfMovesMade--
            setSquareOccupant(move.source, movingPiece)
            setSquareOccupant(move.destination, EmptySquare)
            setSquareOccupant(captivePosition, captives.removeLast())
        }
        is Castling -> {
            val rook = getSquareOccupant(move.secondaryDestination)

            movingPiece.numberOfMovesMade--
            rook.numberOfMovesMade--

            setSquareOccupant(move.source, movingPiece)
            setSquareOccupant(move.destination, EmptySquare)

            setSquareOccupant(move.secondarySource, rook)
            setSquareOccupant(move.secondaryDestination, EmptySquare)
        }
    }

    if (movingPiece.chessPieceType == ChessPieceType.KING) {
        when (movingPiece.chessPieceColor) {
            ChessPieceColor.BLACK -> blackKingPosition = move.source
            ChessPieceColor.WHITE -> whiteKingPosition = move.source
        }
    }

    return move
}