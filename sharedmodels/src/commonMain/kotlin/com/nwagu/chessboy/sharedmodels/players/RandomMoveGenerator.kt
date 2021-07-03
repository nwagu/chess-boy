package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.board.Board
import com.nwagu.chess.board.squaresWithPiecesColored
import com.nwagu.chess.board.turn
import com.nwagu.chess.moves.Move
import com.nwagu.chess.moves.getPossibleMovesFrom
import com.nwagu.chessboy.sharedmodels.utils.ImageRes
import com.nwagu.chessboy.sharedmodels.utils.getRandomMoveGeneratorAvatar

class RandomMoveGenerator : MoveGenerator() {

    override val id = PlayersRegister.RANDOM
    override val name = "Zero Intelligence"
    override var avatar: ImageRes = getRandomMoveGeneratorAvatar()

    override suspend fun getNextMove(board: Board): Move? {

        val moves = mutableListOf<Move>()

        board.squaresWithPiecesColored(board.turn).forEach {
            moves.addAll(board.getPossibleMovesFrom(it))
        }

        return moves.randomOrNull()

    }
}