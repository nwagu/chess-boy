package com.nwagu.android.chessboy.vm

import androidx.lifecycle.ViewModel
import com.nwagu.android.chessboy.movesgenerators.AI
import com.nwagu.android.chessboy.movesgenerators.RandomMoveGenerator
import com.nwagu.chess.enums.ChessPieceColor
import kotlinx.coroutines.flow.MutableStateFlow

class NewGameViewModel: ViewModel() {

    val selectedColor = MutableStateFlow<ChessPieceColor?>(null)
    val selectedOpponent = MutableStateFlow<AI?>(null)

    val opponents = listOf<AI>(
        RandomMoveGenerator(),
        RandomMoveGenerator(),
        RandomMoveGenerator()
    )
}