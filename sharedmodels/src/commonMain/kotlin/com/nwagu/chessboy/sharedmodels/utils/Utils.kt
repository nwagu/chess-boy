package com.nwagu.chessboy.sharedmodels.utils

import com.nwagu.chess.board.ChessPiece
import com.nwagu.chess.enums.SquareColor

expect fun ChessPiece.imageRes(): ImageRes
expect fun SquareColor.colorResource(): ColorRes