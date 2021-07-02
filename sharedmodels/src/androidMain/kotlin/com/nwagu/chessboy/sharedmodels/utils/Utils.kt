package com.nwagu.chessboy.sharedmodels.utils

import com.nwagu.chess.board.ChessPiece
import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.enums.SquareColor
import com.nwagu.chessboy.sharedmodels.R

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
