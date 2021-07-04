package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.model.Board
import com.nwagu.chess.gamelogic.squaresWithPiecesColored
import com.nwagu.chess.gamelogic.turn
import com.nwagu.chess.model.Move
import com.nwagu.chess.gamelogic.moves.getPossibleMovesFrom
import com.nwagu.chessboy.sharedmodels.resources.ImageRes
import com.nwagu.chessboy.sharedmodels.resources.getRandomMoveGeneratorAvatar

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