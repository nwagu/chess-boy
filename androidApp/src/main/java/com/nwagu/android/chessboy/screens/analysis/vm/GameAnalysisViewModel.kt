package com.nwagu.android.chessboy.screens.analysis.vm

import android.app.Application
import com.nwagu.android.chessboy.screens.common.BaseViewModel
import com.nwagu.chess.Game

class GameAnalysisViewModel(application: Application): BaseViewModel(application) {

    lateinit var game: Game

    var pgn: String = ""
    set(value) {
        field = value
        game = recreateGameFromPgn(value)
    }

    fun first() {

    }

    fun previous() {

    }

    fun next() {

    }

    fun last() {

    }

}