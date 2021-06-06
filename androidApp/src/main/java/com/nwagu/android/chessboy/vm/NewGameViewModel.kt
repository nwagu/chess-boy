package com.nwagu.android.chessboy.vm

import androidx.lifecycle.ViewModel
import com.nwagu.android.chessboy.players.AI
import com.nwagu.android.chessboy.players.GrandPa
import com.nwagu.android.chessboy.players.RandomMoveGenerator
import com.nwagu.android.chessboy.players.UCIChessEngine
import com.nwagu.chess.enums.ChessPieceColor
import kotlinx.coroutines.flow.MutableStateFlow

class NewGameViewModel: ViewModel() {

    val selectedColor = MutableStateFlow<ChessPieceColor?>(null)
    val selectedOpponent = MutableStateFlow<AI?>(null)

    val opponents = listOf(
        RandomMoveGenerator(),
        GrandPa(),
        UCIChessEngine(
            name = "StockFish",
            pathToBinary = "/data/data/com.nwagu.android.chessboy/stockfish"
        ),
        UCIChessEngine(
            name = "Komodo",
            pathToBinary = "/data/data/com.nwagu.android.chessboy/stockfish"
        )
    )
}