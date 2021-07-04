package com.nwagu.chessboy.sharedmodels.players

import kotlinx.coroutines.flow.MutableStateFlow

abstract class UCIChessEngine: MoveGenerator() {
    abstract val minLevel: Int
    abstract val maxLevel: Int
    abstract var level: Int
    abstract val connectionState: MutableStateFlow<Boolean>
    abstract fun init()
    abstract fun quit()
}