package com.nwagu.chessboy.sharedmodels.players

import android.content.Context
import com.nwagu.chess.Player

object PlayersRegister {
    const val USER = "User"
    const val GRANDPA = "GRANDPA"
    const val RANDOM = "RANDOM"
    const val JWTC = "JWTC"
    const val BLUETOOTH = "BLUETOOTH"
    const val STOCKFISH = "STOCKFISH"
}

expect fun getPlayerWithId(context: Context, id: String): Player