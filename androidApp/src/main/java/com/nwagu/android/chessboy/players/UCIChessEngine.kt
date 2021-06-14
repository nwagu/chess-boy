package com.nwagu.android.chessboy.players

import kotlinx.coroutines.flow.MutableStateFlow

interface UCIChessEngine: MoveGenerator {
    val minLevel: Int
    val maxLevel: Int
    var level: Int
    val connectionState: MutableStateFlow<Boolean>
    fun init()
    fun quit()
}