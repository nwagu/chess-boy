package com.nwagu.chessboy.sharedmodels.players

import java.util.regex.Pattern

val uciMovePattern = Pattern.compile("([a-h]{1}[1-8]{1})([a-h]{1}[1-8]{1})([qrbn])?")

expect class Stockfish: UCIChessEngine