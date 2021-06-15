package com.nwagu.android.chessboy.players

import android.content.Context
import com.nwagu.chess.Player

object PlayersRegister {
    const val USER = "User"
    const val GRANDPA = "GRANDPA"
    const val RANDOM = "RANDOM"
    const val JWTC = "JWTC"
    const val BLUETOOTH = "BLUETOOTH"
    const val STOCKFISH13 = "STOCKFISH13"
}

fun getPlayerWithId(context: Context, id: String): Player {
    return when {
        id == PlayersRegister.USER -> User
        id == PlayersRegister.GRANDPA -> GrandPa()
        id == PlayersRegister.RANDOM -> RandomMoveGenerator()
        id.startsWith(PlayersRegister.JWTC) -> {
            val level = try {
                id.split("-")[1].split("=")[1].toInt()
            } catch (e: Exception) {
                5
            }
            JWTC().also { it.level = level }
        }
        id.startsWith(PlayersRegister.STOCKFISH13) -> {
            val level = try {
                id.split("-")[1].split("=")[1].toInt()
            } catch (e: Exception) {
                5
            }
            Stockfish13(context).also { it.level = level }
        }
        id.startsWith(PlayersRegister.BLUETOOTH) -> {
            val address = id.split("-")[1]
            BluetoothPlayer(address = address)
        }
        else -> RandomMoveGenerator()
    }
}