package com.nwagu.android.chessboy.vm

import androidx.lifecycle.ViewModel
import com.nwagu.android.chessboy.players.JWTC
import com.nwagu.android.chessboy.players.MoveGenerator
import com.nwagu.android.chessboy.players.RandomMoveGenerator
import com.nwagu.chess.enums.ChessPieceColor
import kotlinx.coroutines.flow.MutableStateFlow

class NewGameViewModel: ViewModel() {

    val selectedColor = MutableStateFlow<ChessPieceColor?>(null)
    val selectedOpponent = MutableStateFlow<MoveGenerator?>(null)

    val opponents = listOf(
        RandomMoveGenerator(),
        /*UCIChessEngine(
            name = "StockFish",
            pathToBinary = "/data/data/com.nwagu.android.chessboy/stockfish"
        ),*/
        JWTC()
    )
}