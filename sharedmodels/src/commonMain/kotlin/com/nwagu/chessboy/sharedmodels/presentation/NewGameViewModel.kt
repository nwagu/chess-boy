package com.nwagu.chessboy.sharedmodels.presentation

import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chess.model.Player
import com.nwagu.chessboy.sharedmodels.players.*
import kotlinx.coroutines.flow.MutableStateFlow

class NewGameViewModel: BaseViewModel() {

    val selectedColor = MutableStateFlow<ChessPieceColor?>(null)
    val selectedOpponent = MutableStateFlow<MoveGenerator?>(null)

    val opponents = listOf(
        Stockfish(),
        JWTC(),
        RandomMoveGenerator()
    )

    fun getSelectedPlayers(): Pair<Player, Player>? {

        selectedOpponent.value?.let { opponent ->

            val whitePlayer = when (selectedColor.value) {
                ChessPieceColor.WHITE -> User
                ChessPieceColor.BLACK -> opponent
                null -> listOf(User, opponent).random()
            }
            val blackPlayer = if (whitePlayer is User) opponent else User

            return Pair(first = whitePlayer, second = blackPlayer)
        }

        return null
    }
}