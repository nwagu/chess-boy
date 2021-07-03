package com.nwagu.chessboy.sharedmodels.players

import com.nwagu.chess.model.Player

fun getPlayerWithId(id: String): Player {
    return when {
        id == PlayersRegister.USER -> User
        id == PlayersRegister.RANDOM -> RandomMoveGenerator()
        id.startsWith(PlayersRegister.JWTC) -> {
            val level = try {
                id.split("-")[1].split("=")[1].toInt()
            } catch (e: Exception) {
                5
            }
            JWTC().also { it.level = level }
        }
        id.startsWith(PlayersRegister.STOCKFISH) -> {
            val level = try {
                id.split("-")[1].split("=")[1].toInt()
            } catch (e: Exception) {
                5
            }
            Stockfish().also { it.level = level }
        }
        id.startsWith(PlayersRegister.BLUETOOTH) -> {
            val address = id.split("-")[1]
            BluetoothPlayer(address = address)
        }
        else -> RandomMoveGenerator()
    }
}