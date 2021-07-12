package com.nwagu.android.chessboy.screens.newgame.vm

import android.app.Application
import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chessboy.sharedmodels.players.*
import com.nwagu.chessboy.sharedmodels.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class NewGameViewModel(application: Application): BaseViewModel(application) {

    val selectedColor = MutableStateFlow<ChessPieceColor?>(null)
    val selectedOpponent = MutableStateFlow<MoveGenerator?>(null)

    val opponents = listOf(
        Stockfish(getApplication()),
        JWTC(),
        RandomMoveGenerator()
    )
}