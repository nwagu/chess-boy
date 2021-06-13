package com.nwagu.android.chessboy.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nwagu.android.chessboy.players.JWTC
import com.nwagu.android.chessboy.players.MoveGenerator
import com.nwagu.android.chessboy.players.RandomMoveGenerator
import com.nwagu.android.chessboy.players.Stockfish13
import com.nwagu.chess.enums.ChessPieceColor
import kotlinx.coroutines.flow.MutableStateFlow

class NewGameViewModel(application: Application): AndroidViewModel(application) {

    val selectedColor = MutableStateFlow<ChessPieceColor?>(null)
    val selectedOpponent = MutableStateFlow<MoveGenerator?>(null)

    val opponents = listOf(
        Stockfish13(getApplication()),
        JWTC(),
        RandomMoveGenerator()
    )
}