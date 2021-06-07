package com.nwagu.android.chessboy.players

enum class PlayersRegister(val id: String) {
    USER(User::class.java.simpleName),
    GRANDPA(GrandPa::class.java.simpleName),
    RANDOM(RandomMoveGenerator::class.java.simpleName),
    JWTC(com.nwagu.android.chessboy.players.JWTC::class.java.simpleName),
    BLUETOOTH(BluetoothOpponent::class.java.simpleName),
    UCI(UCIChessEngine::class.java.simpleName)
}