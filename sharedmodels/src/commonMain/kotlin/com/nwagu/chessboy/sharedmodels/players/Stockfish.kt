package com.nwagu.chessboy.sharedmodels.players

val uciMovePattern = Regex("""([a-h]{1}[1-8]{1})([a-h]{1}[1-8]{1})([qrbn])?""")

expect class Stockfish: UCIChessEngine