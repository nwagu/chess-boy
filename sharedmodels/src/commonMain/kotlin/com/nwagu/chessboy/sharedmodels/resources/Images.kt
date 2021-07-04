package com.nwagu.chessboy.sharedmodels.resources

import com.nwagu.chess.model.ChessPiece

expect class ImageRes

expect fun ChessPiece.imageRes(): ImageRes
expect fun getDefaultPlayerAvatar(): ImageRes
expect fun getBluetoothPlayerAvatar(): ImageRes
expect fun getRandomMoveGeneratorAvatar(): ImageRes