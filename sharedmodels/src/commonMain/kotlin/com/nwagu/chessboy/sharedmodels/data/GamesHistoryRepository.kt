package com.nwagu.chessboy.sharedmodels.data

import com.nwagu.chessboy.GameHistory

interface GamesHistoryRepository {

    fun addGame(id: String, pgn: String)
    fun getLastGame(): GameHistory?
    fun getMostRecentGames(numberOfGames: Int = 20): List<GameHistory>
    fun clearGamesHistory()

}