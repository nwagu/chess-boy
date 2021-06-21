package com.nwagu.android.chessboy.screens.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nwagu.android.chessboy.players.getPlayerWithId
import com.nwagu.chess.Game
import com.nwagu.chess.convention.*

abstract class BaseViewModel(application: Application): AndroidViewModel(application) {

    fun recreateGameFromPgn(pgn: String): Game {

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

}