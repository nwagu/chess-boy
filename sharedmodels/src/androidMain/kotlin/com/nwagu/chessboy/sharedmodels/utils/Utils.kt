package com.nwagu.chessboy.sharedmodels.utils

import com.nwagu.chess.model.ChessPiece
import com.nwagu.chess.model.ChessPieceColor
import com.nwagu.chess.model.ChessPieceType
import com.nwagu.chess.model.SquareColor
import com.nwagu.chessboy.sharedmodels.R

actual typealias ImageRes = /*@DrawableRes*/ Int
actual typealias ColorRes = /*@ColorRes*/ Int

/*@DrawableRes*/
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

/*@ColorRes*/
actual fun SquareColor.colorResource(): ColorRes {
    return when (this) {
        SquareColor.WHITE -> R.color.lightSquare
        SquareColor.BLACK -> R.color.darkSquare
    }
}

actual fun getDefaultPlayerAvatar(): ImageRes = R.drawable.img_avatar_default
actual fun getBluetoothPlayerAvatar(): ImageRes = R.drawable.img_avatar_default
actual fun getRandomMoveGeneratorAvatar(): ImageRes = R.drawable.img_avatar_random
