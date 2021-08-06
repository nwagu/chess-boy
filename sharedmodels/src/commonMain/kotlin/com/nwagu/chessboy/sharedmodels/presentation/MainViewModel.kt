package com.nwagu.chessboy.sharedmodels.presentation

import com.nwagu.bluetoothchat.BluetoothChatService
import com.nwagu.chess.model.Game
import com.nwagu.chess.model.Player
import com.nwagu.chess.representation.*
import com.nwagu.chessboy.sharedmodels.data.SavedGame
import com.nwagu.chessboy.sharedmodels.players.*

class MainViewModel: BaseViewModel() {

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
            throw Exception("Please use createNewBluetoothGame to create bluetooth game!")
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
        gamesHistoryRepository.addGame(game.id, game.exportPGN())
    }

    private fun getLastGamePgn(): String? {
        return gamesHistoryRepository.getLastGame()?.pgn
    }

    fun getGamesHistory(): List<SavedGame> {
        return gamesHistoryRepository.getMostRecentGames(20)
    }

}