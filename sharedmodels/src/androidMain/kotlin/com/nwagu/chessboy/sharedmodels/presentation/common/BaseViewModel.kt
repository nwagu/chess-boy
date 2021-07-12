package com.nwagu.chessboy.sharedmodels.presentation.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nwagu.chess.model.Game
import com.nwagu.chess.representation.*
import com.nwagu.chessboy.sharedmodels.players.getPlayerWithId
import kotlinx.coroutines.CoroutineScope

actual open class BaseViewModel(application: Application): AndroidViewModel(application) {
    actual val clientScope: CoroutineScope = viewModelScope
    actual override fun onCleared() {
        super.onCleared()
    }
}

fun BaseViewModel.recreateGameFromPgn(pgn: String): Game {

    val gameId = getHeaderValueFromPgn(PGN_HEADER_GAME_ID, pgn) ?: ""

    val whitePlayer =
        getPlayerWithId(
            getApplication(),
            getHeaderValueFromPgn(PGN_HEADER_WHITE_PLAYER_ID, pgn) ?: ""
        )

    val blackPlayer =
        getPlayerWithId(
            getApplication(),
            getHeaderValueFromPgn(PGN_HEADER_BLACK_PLAYER_ID, pgn) ?: ""
        )

    val game = Game(gameId, whitePlayer, blackPlayer)
    game.board.importMovesFromPGN(pgn)

    return game
}