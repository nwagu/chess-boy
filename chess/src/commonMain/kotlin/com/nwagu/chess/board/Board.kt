package com.nwagu.chess.board

import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.enums.opposite
import com.nwagu.chess.moves.*
import com.nwagu.chess.positions.populateCellsWithEmpties

typealias Square = Int

class Board(
    val numberOfRows: Int = 8,
    val numberOfColumns: Int = 8
) {

    val squaresMap: HashMap<Int, SquareOccupant> = hashMapOf()
    val movesHistory = ArrayDeque<Move>()
    val captives = ArrayDeque<ChessPiece>()
    var blackKingPosition: Int = 0
    var whiteKingPosition: Int = 0

    init {
        populateCellsWithEmpties()
        movesHistory.clear()
        captives.clear()
    }

    fun positionsWithPiecesColored(color: ChessPieceColor): List<Int> {
        return squaresMap.filter {
            it.value.let {
                it is ChessPiece && it.chessPieceColor == color
            }
        }.map { it.key }
    }

    val turn: ChessPieceColor
    get() {
        try {
            val lastMove = movesHistory.lastOrNull() ?: return ChessPieceColor.WHITE
            return getCellOccupant(lastMove.destination).chessPieceColor.opposite()
        } catch (e: Exception) {
            e.printStackTrace()
            TODO()
        }
    }

    fun getCellOccupant(index: Int): ChessPiece {
        return squaresMap[index] as ChessPiece
    }

    fun setCellOccupant(index: Int, piece: SquareOccupant) {
        squaresMap[index] = piece
    }

    fun move(move: Move): Boolean {

        try {
            getCellOccupant(move.source)
        } catch (e: Exception) {
            return false
        }

        movesHistory.add(move)

        if (getCellOccupant(move.source).chessPieceType == ChessPieceType.KING) {
            when (getCellOccupant(move.source).chessPieceColor) {
                ChessPieceColor.BLACK -> blackKingPosition = move.destination
                ChessPieceColor.WHITE -> whiteKingPosition = move.destination
            }
        }

        when(move) {
            is RegularMove -> {
                val mover = getCellOccupant(move.source)
                mover.numberOfMovesMade++
                squaresMap[move.destination].let { captive ->
                    if (captive is ChessPiece) {
                        move.isCapture = true
                        captives.add(captive)
                    }
                }
                setCellOccupant(move.source, EmptySquare)
                setCellOccupant(move.destination, mover)
            }
            is Promotion -> {
                val pawnToPromote = getCellOccupant(move.source)
                val promotionPiece = ChessPiece(
                    chessPieceType = move.promotionType,
                    chessPieceColor = pawnToPromote.chessPieceColor,
                    numberOfMovesMade = pawnToPromote.numberOfMovesMade++,
                    startingRow = pawnToPromote.startingRow,
                    startingColumn = pawnToPromote.startingColumn
                )
                squaresMap[move.destination].let { captive ->
                    if (captive is ChessPiece) {
                        move.isCapture = true
                        captives.add(captive)
                    }
                }
                setCellOccupant(move.source, EmptySquare)
                setCellOccupant(move.destination, promotionPiece)
            }
            is EnPassant -> {
                move.isCapture = true
                val pawn = getCellOccupant(move.source)
                pawn.numberOfMovesMade++
                captives.add(getCellOccupant(move.captivePosition))
                setCellOccupant(move.source, EmptySquare)
                setCellOccupant(move.destination, pawn)
                setCellOccupant(move.captivePosition, EmptySquare)
            }
            is Castling -> {
                val king = getCellOccupant(move.source)
                val rook = getCellOccupant(move.secondarySource)

                king.numberOfMovesMade++
                rook.numberOfMovesMade++

                setCellOccupant(move.source, EmptySquare)
                setCellOccupant(move.destination, king)

                setCellOccupant(move.secondarySource, EmptySquare)
                setCellOccupant(move.secondaryDestination, rook)
            }
        }

        return true
    }

    fun undoMove(): Move? {
        val move = movesHistory.removeLastOrNull() ?: return null

        when(move) {
            is RegularMove -> {
                val mover = getCellOccupant(move.destination)
                mover.numberOfMovesMade--
                setCellOccupant(move.source, mover)

                if (move.isCapture)
                    setCellOccupant(move.destination, captives.removeLast())
                else
                    setCellOccupant(move.destination, EmptySquare)
            }
            is Promotion -> {
                val promotedPiece = getCellOccupant(move.destination)
                val initialPawn = ChessPiece(
                    chessPieceType = ChessPieceType.PAWN,
                    chessPieceColor = promotedPiece.chessPieceColor,
                    numberOfMovesMade = promotedPiece.numberOfMovesMade--,
                    startingRow = promotedPiece.startingRow,
                    startingColumn = promotedPiece.startingColumn
                )
                setCellOccupant(move.source, initialPawn)

                if (move.isCapture)
                    setCellOccupant(move.destination, captives.removeLast())
                else
                    setCellOccupant(move.destination, EmptySquare)
            }
            is EnPassant -> {
                val pawn = getCellOccupant(move.destination)
                pawn.numberOfMovesMade--
                setCellOccupant(move.source, pawn)
                setCellOccupant(move.destination, EmptySquare)
                setCellOccupant(move.captivePosition, captives.removeLast())
            }
            is Castling -> {
                val king = getCellOccupant(move.destination)
                val rook = getCellOccupant(move.secondaryDestination)

                king.numberOfMovesMade--
                rook.numberOfMovesMade--

                setCellOccupant(move.source, king)
                setCellOccupant(move.destination, EmptySquare)

                setCellOccupant(move.secondarySource, rook)
                setCellOccupant(move.secondaryDestination, EmptySquare)
            }
        }

        if (getCellOccupant(move.source).chessPieceType == ChessPieceType.KING) {
            when (getCellOccupant(move.source).chessPieceColor) {
                ChessPieceColor.BLACK -> blackKingPosition = move.source
                ChessPieceColor.WHITE -> whiteKingPosition = move.source
            }
        }

        return move
    }

}