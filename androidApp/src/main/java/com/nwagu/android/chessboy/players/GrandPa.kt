package com.nwagu.android.chessboy.players

import com.nwagu.chess.board.Board
import com.nwagu.chess.board.squaresWithPiecesColored
import com.nwagu.chess.board.turn
import com.nwagu.chess.moves.Move
import com.nwagu.chess.moves.getPossibleMovesFrom

class GrandPa : AI {

    override val name = "GrandPa"

    override suspend fun getNextMove(board: Board): Move? {

        val moves = mutableListOf<Move>()

        board.squaresWithPiecesColored(board.turn).forEach {
            moves.addAll(board.getPossibleMovesFrom(it))
        }

        // TODO return a decent choice of move
        return moves.randomOrNull()

    }
}