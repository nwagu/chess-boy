package com.nwagu.chess.convention

import com.nwagu.chess.board.*

/*
* All the methods here will only work for the regular variant of chess
* */

val Board.isWhiteQueenSideCastlingAvailable: Boolean
    get() {
        return (getSquareOccupant(whiteKingPosition).numberOfMovesMade == 0) &&
                run {
                    val queenSideCastlingRookSquare = square(row(whiteKingPosition), column(whiteKingPosition) - 4)
                    squareOccupantHasNotMoved(queenSideCastlingRookSquare)
                }
    }

val Board.isWhiteKingSideCastlingAvailable: Boolean
    get() {
        return (getSquareOccupant(whiteKingPosition).numberOfMovesMade == 0) &&
                run {
                    val kingSideCastlingRookSquare = square(row(whiteKingPosition), column(whiteKingPosition) + 3)
                    squareOccupantHasNotMoved(kingSideCastlingRookSquare)
                }
    }

val Board.isBlackQueenSideCastlingAvailable: Boolean
    get() {
        return (getSquareOccupant(blackKingPosition).numberOfMovesMade == 0) &&
                run {
                    val queenSideCastlingRookSquare = square(row(blackKingPosition), column(blackKingPosition) - 4)
                    squareOccupantHasNotMoved(queenSideCastlingRookSquare)
                }
    }

val Board.isBlackKingSideCastlingAvailable: Boolean
    get() {
        return (getSquareOccupant(blackKingPosition).numberOfMovesMade == 0) &&
                run {
                    val kingSideCastlingRookSquare = square(row(blackKingPosition), column(blackKingPosition) + 3)
                    squareOccupantHasNotMoved(kingSideCastlingRookSquare)
                }
    }