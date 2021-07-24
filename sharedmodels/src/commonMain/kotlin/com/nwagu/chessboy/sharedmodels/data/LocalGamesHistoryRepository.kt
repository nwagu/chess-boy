package com.nwagu.chessboy.sharedmodels.data

import com.nwagu.chessboy.ChessBoyDatabase
import com.nwagu.chessboy.GameHistory

class LocalGamesHistoryRepository(
    databaseDriverFactory: DatabaseDriverFactory
) : GamesHistoryRepository {

    private val database = ChessBoyDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.gameHistoryQueries

    override fun addGame(id: String, pgn: String) {
        dbQuery.insertGame(id, pgn)
    }

    override fun getLastGame(): GameHistory? {
        return dbQuery.selectLastGame().executeAsOneOrNull()
    }

    override fun getMostRecentGames(numberOfGames: Int): List<GameHistory> {
        return dbQuery.mostRecentGames(numberOfGames.toLong()).executeAsList()
    }

    override fun clearGamesHistory() {
        dbQuery.removeAllGames()
    }

}