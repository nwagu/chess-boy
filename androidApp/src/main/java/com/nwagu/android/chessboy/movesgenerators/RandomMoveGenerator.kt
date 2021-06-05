package com.nwagu.android.chessboy.movesgenerators

import com.nwagu.android.chessboy.widgets.SelectableOpponent
import com.nwagu.chess.board.Board
import com.nwagu.chess.board.squaresWithPiecesColored
import com.nwagu.chess.board.turn
import com.nwagu.chess.enums.Level
import com.nwagu.chess.moves.Move
import com.nwagu.chess.moves.getPossibleMovesFrom

class RandomMoveGenerator : AI, SelectableOpponent {

    override val name = "Zero Intelligence"
    override val level = Level.ZERO

    override suspend fun getNextMove(board: Board): Move? {

        val moves = mutableListOf<Move>()

        board.squaresWithPiecesColored(board.turn).forEach {
            moves.addAll(board.getPossibleMovesFrom(it))
        }

        return moves.randomOrNull()

    }

    override val displayName = name
    override var selected = false
}