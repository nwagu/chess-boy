package com.nwagu.chess.gamelogic

import com.nwagu.chess.model.*
import com.nwagu.chess.representation.attachSanToMove
import com.nwagu.chess.gamelogic.moves.*

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

            val rookSource = getCastlePartnerSourceForKingMove(move.source, move.destination)
            val rookDestination = getCastlePartnerDestinationForKingMove(move.source, move.destination)

            val rook = getSquareOccupant(rookSource)

            movingPiece.numberOfMovesMade++
            rook.numberOfMovesMade++

            setSquareOccupant(move.source, EmptySquare)
            setSquareOccupant(move.destination, movingPiece)

            setSquareOccupant(rookSource, EmptySquare)
            setSquareOccupant(rookDestination, rook)
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
            val rookSource = getCastlePartnerSourceForKingMove(move.source, move.destination)
            val rookDestination = getCastlePartnerDestinationForKingMove(move.source, move.destination)

            val rook = getSquareOccupant(rookDestination)

            movingPiece.numberOfMovesMade--
            rook.numberOfMovesMade--

            setSquareOccupant(move.source, movingPiece)
            setSquareOccupant(move.destination, EmptySquare)

            setSquareOccupant(rookSource, rook)
            setSquareOccupant(rookDestination, EmptySquare)
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