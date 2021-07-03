package com.nwagu.chessboy.sharedmodels.utils

import com.nwagu.chess.model.ChessPiece
import com.nwagu.chess.model.SquareColor

expect class ImageRes
expect class ColorRes

expect fun ChessPiece.imageRes(): ImageRes
expect fun SquareColor.colorResource(): ColorRes
expect fun getDefaultPlayerAvatar(): ImageRes
expect fun getBluetoothPlayerAvatar(): ImageRes
expect fun getRandomMoveGeneratorAvatar(): ImageRes