package com.nwagu.chessboy.sharedmodels.utils

import com.nwagu.chess.board.ChessPiece
import com.nwagu.chess.enums.ChessPieceColor
import com.nwagu.chess.enums.ChessPieceType
import com.nwagu.chess.enums.SquareColor
import com.nwagu.chessboy.sharedmodels.utils.ColorRes
import com.nwagu.chessboy.sharedmodels.utils.ImageRes

actual fun ChessPiece.imageRes(): ImageRes {
    return when (chessPieceType) {
        ChessPieceType.QUEEN -> if (chessPieceColor == ChessPieceColor.WHITE) "img_white_queen" else "img_black_queen"
        ChessPieceType.KING -> if (chessPieceColor == ChessPieceColor.WHITE) "img_white_king" else "img_black_king"
        ChessPieceType.KNIGHT -> if (chessPieceColor == ChessPieceColor.WHITE) "img_white_knight" else "img_black_knight"
        ChessPieceType.BISHOP -> if (chessPieceColor == ChessPieceColor.WHITE) "img_white_bishop" else "img_black_bishop"
        ChessPieceType.ROOK -> if (chessPieceColor == ChessPieceColor.WHITE) "img_white_rook" else "img_black_rook"
        ChessPieceType.PAWN -> if (chessPieceColor == ChessPieceColor.WHITE) "img_white_pawn" else "img_black_pawn"
    }
}

actual fun SquareColor.colorResource(): ColorRes {
    return when (this) {
        SquareColor.WHITE -> "white"
        SquareColor.BLACK -> "black"
    }
}

actual fun getDefaultPlayerAvatar(): ImageRes = "img_avatar_default"