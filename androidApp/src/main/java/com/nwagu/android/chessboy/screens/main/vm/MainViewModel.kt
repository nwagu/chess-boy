package com.nwagu.android.chessboy.screens.main.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.nwagu.android.chessboy.players.*
import com.nwagu.android.chessboy.screens.common.BaseViewModel
import com.nwagu.android.chessboy.util.SharedPrefUtils.getSavedPGNs
import com.nwagu.android.chessboy.util.SharedPrefUtils.savePGNs
import com.nwagu.bluetoothchat.BluetoothChatService
import com.nwagu.chess.Game
import com.nwagu.chess.Player
import com.nwagu.chess.convention.*
import kotlinx.coroutines.launch
import java.security.InvalidParameterException

class MainViewModel(application: Application): BaseViewModel(application) {

    fun getLastGameOrDefault(): Game {

        val lastGamePgn = getLastGamePgn()

        if (lastGamePgn.isNullOrEmpty()) {
            return Game(
                whitePlayer = User,
                blackPlayer = RandomMoveGenerator()
            )
        }

        return recreateGameFromPgn(lastGamePgn)
    }

    fun createNewGame(whitePlayer: Player, blackPlayer: Player): Game {

        if (whitePlayer is BluetoothPlayer || blackPlayer is BluetoothPlayer) {
            throw InvalidParameterException("Please use createNewBluetoothGame to create bluetooth game!")
        }

        return Game(
            whitePlayer = whitePlayer,
            blackPlayer = blackPlayer
        )
    }

    fun createNewBluetoothGame(bluetoothChatService: BluetoothChatService): Game {

        val isWhite = bluetoothChatService.isInitiator
        val address = bluetoothChatService.partnerAddress

        return Game(
            whitePlayer = if (isWhite) User else BluetoothPlayer(address = address),
            blackPlayer = if (isWhite) BluetoothPlayer(address = address) else User
        )
    }

    fun saveGame(game: Game) {
        viewModelScope.launch {
            val history = getGamesHistory()

            while (history.size > 20) {
                history.removeFirst()
            }

            history.removeAll(history.filter {
                getHeaderValueFromPgn(PGN_HEADER_GAME_ID, it) == game.id
            })
            history.addLast(game.exportPGN())

            savePGNs(getApplication(), history.toList())
        }
    }

    private fun getLastGamePgn(): String? {
        return getGamesHistory().lastOrNull()
    }

    fun getGamesHistory(): ArrayDeque<String> {
        return ArrayDeque(getSavedPGNs(getApplication()))
    }

    companion object {


    }

}