package com.nwagu.chessboy.sharedmodels.players

import android.content.Context
import com.nwagu.chess.Player

fun getPlayerWithId(context: Context, id: String): Player {
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
            Stockfish(context).also { it.level = level }
        }
        id.startsWith(PlayersRegister.BLUETOOTH) -> {
            val address = id.split("-")[1]
            BluetoothPlayer(address = address)
        }
        else -> RandomMoveGenerator()
    }
}