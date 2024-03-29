package com.nwagu.chessboy.sharedmodels.presentation

import com.nwagu.chess.model.Game
import com.nwagu.chess.gamelogic.move
import com.nwagu.chess.gamelogic.undoMove
import com.nwagu.chess.representation.sanToMove
import com.nwagu.chessboy.sharedmodels.data.SavedGame
import kotlinx.coroutines.flow.MutableStateFlow

class GameAnalysisViewModel: BaseViewModel() {

    lateinit var game: Game
    private set

    var sans = emptyList<String>()

    val boardUpdated = MutableStateFlow(0)

    var savedGame: SavedGame? = null
    set(value) {
        field = value
        game = recreateGameFromPgn(value?.pgn ?: return)
        sans = game.board.movesHistory.map { it.san }
    }

    val lastMoveIndex: Int
        get() = game.board.movesHistory.size - 1

    fun first() {
        jumpToMove(0)
    }

    fun previous() {
        jumpToMove(lastMoveIndex - 1)
    }

    fun next() {
        jumpToMove(lastMoveIndex + 1)
    }

    fun last() {
        jumpToMove(sans.size - 1)
    }

    fun jumpToMove(index: Int) {

        if (sans.isEmpty())
            return

        if (index > sans.size - 1)
            return

        if (index < 0)
            return

        when {
            (index >= lastMoveIndex + 1) -> {
                for (san in sans.subList(lastMoveIndex + 1, index + 1)) {
                    game.board.move(game.board.sanToMove(san))
                }
            }
            (index < lastMoveIndex) -> {
                repeat(lastMoveIndex - index) {
                    game.board.undoMove()
                }
            }
        }

        updateBoardUI()

    }

    private fun updateBoardUI() {
        if (boardUpdated.value == 0) boardUpdated.value = 1 else boardUpdated.value = 0
    }

}