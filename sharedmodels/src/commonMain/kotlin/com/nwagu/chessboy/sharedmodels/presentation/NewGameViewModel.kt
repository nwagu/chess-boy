package com.nwagu.chessboy.sharedmodels.presentation

import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chessboy.sharedmodels.players.JWTC
import com.nwagu.chessboy.sharedmodels.players.MoveGenerator
import com.nwagu.chessboy.sharedmodels.players.RandomMoveGenerator
import com.nwagu.chessboy.sharedmodels.players.Stockfish
import kotlinx.coroutines.flow.MutableStateFlow

class NewGameViewModel: BaseViewModel() {

    val selectedColor = MutableStateFlow<ChessPieceColor?>(null)
    val selectedOpponent = MutableStateFlow<MoveGenerator?>(null)

    val opponents = listOf(
        Stockfish(),
        JWTC(),
        RandomMoveGenerator()
    )
}