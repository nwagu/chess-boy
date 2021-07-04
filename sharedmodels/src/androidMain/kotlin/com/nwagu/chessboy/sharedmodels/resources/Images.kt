package com.nwagu.chessboy.sharedmodels.resources

import com.nwagu.chess.model.ChessPiece
import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chess.model.ChessPieceType
import com.nwagu.chessboy.sharedmodels.R

actual typealias ImageRes = Int

actual fun ChessPiece.imageRes(): ImageRes {
    return when (chessPieceType) {
        ChessPieceType.QUEEN -> if (chessPieceColor == ChessPieceColor.WHITE) R.drawable.img_white_queen else R.drawable.img_black_queen
        ChessPieceType.KING -> if (chessPieceColor == ChessPieceColor.WHITE) R.drawable.img_white_king else R.drawable.img_black_king
        ChessPieceType.KNIGHT -> if (chessPieceColor == ChessPieceColor.WHITE) R.drawable.img_white_knight else R.drawable.img_black_knight
        ChessPieceType.BISHOP -> if (chessPieceColor == ChessPieceColor.WHITE) R.drawable.img_white_bishop else R.drawable.img_black_bishop
        ChessPieceType.ROOK -> if (chessPieceColor == ChessPieceColor.WHITE) R.drawable.img_white_rook else R.drawable.img_black_rook
        ChessPieceType.PAWN -> if (chessPieceColor == ChessPieceColor.WHITE) R.drawable.img_white_pawn else R.drawable.img_black_pawn
    }
}
actual fun getDefaultPlayerAvatar(): ImageRes = R.drawable.img_avatar_default
actual fun getBluetoothPlayerAvatar(): ImageRes = R.drawable.img_avatar_default
actual fun getRandomMoveGeneratorAvatar(): ImageRes = R.drawable.img_avatar_random
