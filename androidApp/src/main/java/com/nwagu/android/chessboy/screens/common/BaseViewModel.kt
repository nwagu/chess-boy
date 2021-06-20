package com.nwagu.android.chessboy.screens.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nwagu.android.chessboy.players.UCIChessEngine
import com.nwagu.android.chessboy.players.getPlayerWithId
import com.nwagu.chess.Game
import com.nwagu.chess.convention.*

abstract class BaseViewModel(application: Application): AndroidViewModel(application) {

    fun recreateGameFromPgn(pgn: String): Game {

        val lastGameId = getHeaderValueFromPgn(PGN_HEADER_GAME_ID, pgn) ?: ""

        val lastWhitePlayer =
            getPlayerWithId(
                getApplication(),
                getHeaderValueFromPgn(PGN_HEADER_WHITE_PLAYER_ID, pgn) ?: ""
            ).apply { if (this is UCIChessEngine) init() }

        val lastBlackPlayer =
            getPlayerWithId(
                getApplication(),
                getHeaderValueFromPgn(PGN_HEADER_BLACK_PLAYER_ID, pgn) ?: ""
            ).apply { if (this is UCIChessEngine) init() }

        val game = Game(lastGameId, lastWhitePlayer, lastBlackPlayer)
        game.board.importMovesFromPGN(pgn)

        return game
    }

}