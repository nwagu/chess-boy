package com.nwagu.android.chessboy.screens.newgame.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nwagu.android.chessboy.players.JWTC
import com.nwagu.android.chessboy.players.MoveGenerator
import com.nwagu.android.chessboy.players.RandomMoveGenerator
import com.nwagu.android.chessboy.players.Stockfish
import com.nwagu.android.chessboy.screens.common.BaseViewModel
import com.nwagu.chess.enums.ChessPieceColor
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