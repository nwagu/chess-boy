package com.nwagu.android.chessboy.screens.analysis.vm

import android.app.Application
import com.nwagu.chess.model.Game
import com.nwagu.chess.gamelogic.move
import com.nwagu.chess.gamelogic.undoMove
import com.nwagu.chess.representation.sanToMove
import com.nwagu.chessboy.sharedmodels.presentation.common.BaseViewModel
import com.nwagu.chessboy.sharedmodels.presentation.common.recreateGameFromPgn
import kotlinx.coroutines.flow.MutableStateFlow

class GameAnalysisViewModel(application: Application): BaseViewModel(application) {

    lateinit var game: Game
    var sans = emptyList<String>()

    val boardUpdated = MutableStateFlow(0)

    var pgn: String = ""
    set(value) {
        field = value
        game = recreateGameFromPgn(value)
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