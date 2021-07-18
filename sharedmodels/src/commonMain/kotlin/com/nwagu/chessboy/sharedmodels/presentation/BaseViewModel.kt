package com.nwagu.chessboy.sharedmodels.presentation

import com.nwagu.chess.model.Game
import com.nwagu.chess.representation.*
import com.nwagu.chessboy.sharedmodels.players.getPlayerWithId
import kotlinx.coroutines.CoroutineScope

expect open class BaseViewModel() {
    val clientScope: CoroutineScope
    protected open fun onCleared()

    fun showToast(message: String)
}

fun recreateGameFromPgn(pgn: String): Game {

    val gameId = getHeaderValueFromPgn(PGN_HEADER_GAME_ID, pgn) ?: ""

    val whitePlayer =
        getPlayerWithId(
            getHeaderValueFromPgn(PGN_HEADER_WHITE_PLAYER_ID, pgn) ?: ""
        )

    val blackPlayer =
        getPlayerWithId(
            getHeaderValueFromPgn(PGN_HEADER_BLACK_PLAYER_ID, pgn) ?: ""
        )

    val game = Game(gameId, whitePlayer, blackPlayer)
    game.board.importMovesFromPGN(pgn)

    return game
}